package net.suweya.photopicker;

import android.content.Context;
import android.util.SparseBooleanArray;

import net.suweya.photopicker.base.BasePresenter;
import net.suweya.photopicker.entity.Folder;
import net.suweya.photopicker.entity.GalleryBundle;
import net.suweya.photopicker.entity.Image;

import java.util.ArrayList;

import rx.Observable;

/**
 * PhotoPickerContract
 * Created by suweya on 16/4/9.
 */
public interface PhotoPickerContract {

    interface Presenter extends BasePresenter {

        void fetchGalleryData(Context context);

        ArrayList<Folder> fetchFolderData();

        void filterFolderImage(int position);

        boolean isMaxImageSelected(SparseBooleanArray array);

        ArrayList<String> getSelectedImagePath();
    }

    interface View {

        void showGallery(ArrayList<Image> images);

        void showFilterGallery(ArrayList<Image> images, boolean showCameraGrid);

        void showFolderPickerPop();

        void changeGalleryName(String categoryName);

        void showToast(int message);
    }

    interface Model {

        Observable<GalleryBundle> provideGalleryData(Context context);
    }
}
