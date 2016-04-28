package net.suweya.photopicker.preview;

import android.os.Bundle;

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
    }

    interface View {
        void setUpViewPager(ArrayList<Image> images, int position);
    }
}
