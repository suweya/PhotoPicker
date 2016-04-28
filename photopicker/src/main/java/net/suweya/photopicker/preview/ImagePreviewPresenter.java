package net.suweya.photopicker.preview;

import android.os.Bundle;

import net.suweya.photopicker.ImageData;
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
        ArrayList<Image> allImages = ImageData.getInstance().getAllImages();
        if (allImages == null || bundle == null) return;

        ArrayList<Integer> selectedList = bundle.getIntegerArrayList(ImagePreviewActivity.KEY_CHECKED_POS_ARRAY);

        ArrayList<Image> data;
        if (selectedList == null || selectedList.size() == 0) {
            // all image
            data = allImages;
        } else {
            data = new ArrayList<>(selectedList.size());
            for (Integer index : selectedList) {
                data.add(allImages.get(index));
            }
        }

        int position = bundle.getInt(ImagePreviewFragment.CURRENT_ITEM);
        mView.setUpViewPager(data, position);
    }

    @Override
    public void unsubscribe() {
    }
}
