package net.suweya.photopicker.preview;

import android.os.Bundle;
import android.util.SparseBooleanArray;

import net.suweya.photopicker.base.BasePresenter;
import net.suweya.photopicker.entity.Image;

import java.util.ArrayList;

/**
 * ImagePreview Contract
 * Created by suweya on 16/4/24.
 */
public interface ImagePreviewContract {

    interface Presenter extends BasePresenter {
        void filterCheckedImageList(Bundle bundle);

        boolean isMaxImageSelected(SparseBooleanArray array);
    }

    interface View {
        void setUpViewPager(ArrayList<Image> images, int position);

        void showToast(int message);
    }
}
