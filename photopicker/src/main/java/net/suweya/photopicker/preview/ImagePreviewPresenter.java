package net.suweya.photopicker.preview;

import android.os.Bundle;
import android.util.SparseBooleanArray;

import net.suweya.photopicker.ImageData;
import net.suweya.photopicker.R;
import net.suweya.photopicker.entity.Image;

import java.util.ArrayList;

/**
 * Image Preview Presenter
 * Created by suweya on 16/4/24.
 */
public class ImagePreviewPresenter implements ImagePreviewContract.Presenter {

    private ImagePreviewContract.View mView;

    public ImagePreviewPresenter(ImagePreviewContract.View view) {
        mView = view;
    }

    @Override
    public void filterCheckedImageList(Bundle bundle) {

        int type = bundle.getInt(ImagePreviewFragment.KEY_PREVIEW_TYPE, ImagePreviewFragment.TYPE_ITEM_PREVIEW);

        ArrayList<Image> images;
        if (type == ImagePreviewFragment.TYPE_CHECKED_PREVIEW) {
            ArrayList<Image> allImages = ImageData.getInstance().getAllImages();
            if (allImages == null) return;

            ArrayList<Integer> selectedList = bundle.getIntegerArrayList(ImagePreviewActivity.KEY_CHECKED_POS_ARRAY);

            if (selectedList == null || selectedList.size() == 0) {
                // category all image
                images = allImages;
            } else {
                images = new ArrayList<>(selectedList.size());
                for (Integer index : selectedList) {
                    images.add(allImages.get(index));
                }
            }
        } else {
            images = ImageData.getInstance().getImages();
        }

        int position = bundle.getInt(ImagePreviewFragment.CURRENT_ITEM);
        mView.setUpViewPager(images, position);
    }

    @Override
    public boolean isMaxImageSelected(SparseBooleanArray array) {
        if (array != null && array.size() >= 9) {
            mView.showToast(R.string.max_images);
            return true;
        }
        return false;
    }

    @Override
    public void unsubscribe() {
    }
}
