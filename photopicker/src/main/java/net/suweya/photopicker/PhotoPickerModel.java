package net.suweya.photopicker;

import android.content.Context;

import net.suweya.photopicker.entity.GalleryBundle;
import net.suweya.photopicker.util.GalleryUtils;

import rx.Observable;
import rx.functions.Func0;

/**
 * PhotoPickerModel
 * Created by suweya on 16/4/9.
 */
public class PhotoPickerModel implements PhotoPickerContract.Model {

    @Override
    public Observable<GalleryBundle> provideGalleryData(final Context context) {
        return Observable.defer(new Func0<Observable<GalleryBundle>>() {
            @Override
            public Observable<GalleryBundle> call() {
                return Observable.just(GalleryUtils.getAllGalleryImage(context));
            }
        });
    }

}
