package net.suweya.photopicker;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

        boolean isMaxImageSelected(Context context, SparseBooleanArray array);

        ArrayList<String> getSelectedImagePath();

        void takePhoto(@NonNull Fragment fragment);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface View {

        void showGallery(ArrayList<Image> images);

        void showFilterGallery(ArrayList<Image> images, boolean showCameraGrid);

        void showFolderPickerPop();

        void changeGalleryName(String categoryName);

        void showToast(String message);

        void takePhoto();

        void finishWithCamera(@NonNull String path);
    }

    interface Model {

        Observable<GalleryBundle> provideGalleryData(Context context);
    }
}
