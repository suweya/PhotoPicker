package net.suweya.photopicker.preview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.suweya.photopicker.ImageData;
import net.suweya.photopicker.R;
import net.suweya.photopicker.entity.Image;

import java.util.ArrayList;

/**
 * ImagePreviewFragment
 * Created by suweya on 16/4/10.
 */
public class ImagePreviewFragment extends Fragment {

    public static final String CURRENT_ITEM = "CURRENT_ITEM";

    public static ImagePreviewFragment newInstance(int item) {

        Bundle args = new Bundle();
        args.putInt(CURRENT_ITEM, item);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ImagePreviewActivity mImagePreviewActivity;

    public ImagePreviewFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mImagePreviewActivity = (ImagePreviewActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ft_image_preview, container, false);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.pager);
        ArrayList<Image> images = ImageData.getInstance().getImages();

        if (images != null) {
            final int imageCount = images.size();
            mViewPager.setAdapter(new ImagePreviewAdapter(getChildFragmentManager(), images));
            int currentItem = getArguments().getInt(CURRENT_ITEM);
            mViewPager.setCurrentItem(currentItem);
            mImagePreviewActivity.setTitle((currentItem+1) + "/" + imageCount);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    mImagePreviewActivity.setTitle((position+1) + "/" + imageCount);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        return view;
    }
}
