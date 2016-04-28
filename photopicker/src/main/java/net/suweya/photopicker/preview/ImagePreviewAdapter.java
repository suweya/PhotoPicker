package net.suweya.photopicker.preview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import net.suweya.photopicker.entity.Image;

import java.util.ArrayList;

/**
 * ImagePreviewAdapter
 * Created by suweya on 16/4/10.
 */
public class ImagePreviewAdapter extends FragmentPagerAdapter {

    private ArrayList<Image> mImages;

    public ImagePreviewAdapter(FragmentManager fm, ArrayList<Image> images) {
        super(fm);
        this.mImages = images;
    }

    @Override
    public Fragment getItem(int position) {
        Image image = mImages.get(position);
        return ImageFragment.newInstance(image.path, image.type);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

}
