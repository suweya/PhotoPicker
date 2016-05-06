package net.suweya.photopicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;

import net.suweya.photopicker.entity.Folder;
import net.suweya.photopicker.entity.GalleryBundle;
import net.suweya.photopicker.entity.Image;
import net.suweya.photopicker.util.FileUtil;

import java.io.File;
import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * PhotoPickerPresenter
 * Created by suweya on 16/4/9.
 */
public class PhotoPickerPresenter implements PhotoPickerContract.Presenter {

    public static final int MAX_IMAGE_SELECTED = 9;
    public static final int DEFAULT_CAMERA_GRID_POSITION = 0;
    private static final int REQUEST_TAKE_PHOTO = 20;

    private PhotoPickerContract.View mView;
    private PhotoPickerContract.Model mModel;

    private Subscription mSubscription;

    private ArrayList<Folder> mFolders;

    private File mImageFile;

    public PhotoPickerPresenter(PhotoPickerContract.View view, PhotoPickerContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mFolders = null;
        mImageFile = null;
    }

    @Override
    public void fetchGalleryData(Context context) {
        mSubscription = mModel.provideGalleryData(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GalleryBundle>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GalleryBundle bundle) {
                        mView.showGallery(bundle.images);
                        mFolders = bundle.folders;
                    }
                });
    }

    @Nullable
    @Override
    public ArrayList<Folder> fetchFolderData() {
        return mFolders;
    }

    @Override
    public void filterFolderImage(int position) {
        if (mFolders != null) {
            mView.showFilterGallery(mFolders.get(position).images,
                    position == DEFAULT_CAMERA_GRID_POSITION);
            mView.changeGalleryName(mFolders.get(position).name);
        }
    }

    @Override
    public boolean isMaxImageSelected(SparseBooleanArray array) {
        if (array != null && array.size() >= MAX_IMAGE_SELECTED) {
            mView.showToast(R.string.max_images);
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public ArrayList<String> getSelectedImagePath() {
        ArrayList<Image> allImages = ImageData.getInstance().getAllImages();
        SparseBooleanArray selectedArray = ImageData.getInstance().getCheckedArray();
        if (allImages != null && selectedArray != null) {
            int count = selectedArray.size();
            ArrayList<String> selectedImages = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                selectedImages.add(allImages.get(selectedArray.keyAt(i)).path);
            }
            return selectedImages;
        }
        return null;
    }

    @Override
    public void takePhoto(@NonNull Fragment fragment) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(fragment.getContext().getPackageManager()) != null) {
            File photoFile = FileUtil.createTakePhotoImageFile(
                    fragment.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            if (photoFile != null) {
                mImageFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                fragment.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK && mImageFile != null) {
            mView.finishWithCamera(mImageFile.getAbsolutePath());
        }
    }
}
