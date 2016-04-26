package net.suweya.photopicker.preview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import net.suweya.photopicker.R;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;
import uk.co.senab.photoview.PhotoView;

/**
 * ImageFragment
 * Created by suweya on 16/4/26.
 */
public class ImageFragment extends Fragment {

    private static final String KEY_PATH = "PATH";
    public static final int ANIMATION_DURATION = 500;

    public static ImageFragment newInstance(String path) {

        Bundle args = new Bundle();
        args.putString(KEY_PATH, path);
        
        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private SupportAnimator mSupportAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ft_preview_image, container, false);

        String path = getArguments().getString(KEY_PATH);

        //final View layout = view.findViewById(R.id.layout);
        final PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        final CircularProgressView progressView = (CircularProgressView) view.findViewById(R.id.progress_view);

        Glide.with(getContext())
                .load(path)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        // Cannot start this animator on a detached view!
                        if (!isDetached()) {
                            playAnimation(photoView);
                            progressView.setVisibility(View.GONE);
                        }
                        return false;
                    }
                })
                .dontAnimate()
                .into(photoView);

        return view;
    }

    private void playAnimation(View v) {
        // get the center for the clipping circle
        int cx = (v.getLeft() + v.getRight()) / 2;
        int cy = (v.getTop() + v.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, v.getWidth() - cx);
        int dy = Math.max(cy, v.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        mSupportAnimator = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
        mSupportAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mSupportAnimator.setDuration(ANIMATION_DURATION);
        mSupportAnimator.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSupportAnimator != null) {
            mSupportAnimator.cancel();
        }
    }
}
