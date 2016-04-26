package net.suweya.photopicker.preview;

import android.util.SparseBooleanArray;

import net.suweya.photopicker.ImageData;
import net.suweya.photopicker.entity.Image;

import java.util.ArrayList;

/**
 * Image Preview Presenter
 * Created by suweya on 16/4/24.
 */
public class ImagePreviewPresenter implements ImagePreviewContract.Presenter {

    @Override
    public ArrayList<Image> filterCheckedImageList(SparseBooleanArray checkedArray) {
        ArrayList<Image> allImages = ImageData.getInstance().getImages();
        ArrayList<Image> checkedImageList = null;
        if (allImages != null && checkedArray != null && checkedArray.size() > 0) {
            final int checkedCount = checkedArray.size();
            checkedImageList = new ArrayList<>(checkedCount);
            for (int i = 0; i < checkedCount; i++) {
                checkedImageList.add(allImages.get(checkedArray.keyAt(i)));
            }
        }
        return checkedImageList;
    }

    @Override
    public void unsubscribe() {

    }
}
