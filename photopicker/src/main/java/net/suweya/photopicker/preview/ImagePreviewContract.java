package net.suweya.photopicker.preview;

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
        ArrayList<Image> filterCheckedImageList(SparseBooleanArray checkedArray);
    }

    interface View {
    }
}
