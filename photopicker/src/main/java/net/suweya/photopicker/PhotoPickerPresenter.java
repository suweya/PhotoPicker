package net.suweya.photopicker;

import android.content.Context;
import android.support.annotation.Nullable;

import net.suweya.photopicker.entity.Folder;
import net.suweya.photopicker.entity.GalleryBundle;

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

    public static final int DEFAULT_CAMERA_GRID_POSITION = 0;

    private PhotoPickerContract.View mView;
    private PhotoPickerContract.Model mModel;

    private Subscription mSubscription;

    private ArrayList<Folder> mFolders;

    public PhotoPickerPresenter(PhotoPickerContract.View view, PhotoPickerContract.Model model) {
        mView = view;
        mModel = model;
    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
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
        }
    }
}
