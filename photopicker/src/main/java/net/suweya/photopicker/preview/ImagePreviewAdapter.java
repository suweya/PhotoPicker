package net.suweya.photopicker.preview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import net.suweya.photopicker.R;
import net.suweya.photopicker.entity.Image;

import java.util.ArrayList;

/**
 * ImagePreviewAdapter
 * Created by suweya on 16/4/10.
 */
public class ImagePreviewAdapter extends PagerAdapter {

    private ArrayList<Image> mImages;

    public ImagePreviewAdapter(@NonNull ArrayList<Image> images) {
        mImages = images;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Context context = container.getContext();
        ImageView view = new ImageView(context);

        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context)
                .load(mImages.get(position).path)
                .placeholder(R.drawable.ic_photo_size_select_actual_black_24dp)
                .into(view);

        //PhotoViewAttacher attacher = new PhotoViewAttacher(view);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
