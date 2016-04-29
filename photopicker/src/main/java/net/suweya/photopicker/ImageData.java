package net.suweya.photopicker;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;

import net.suweya.photopicker.entity.Image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * ImageData
 * Created by suweya on 16/4/10.
 */
public class ImageData {

    static class ImageDataHolder {
        static final ImageData INSTANCE = new ImageData();
    }

    public static ImageData getInstance() {
        return ImageDataHolder.INSTANCE;
    }

    private WeakReference<ArrayList<Image>> images;

    private WeakReference<ArrayList<Image>> allImages;

    @Nullable
    public ArrayList<Image> getImages() {
        return images == null ? null : images.get();
    }

    public void setImages(@NonNull ArrayList<Image> images) {
        this.images = new WeakReference<>(images);
    }

    @Nullable
    public ArrayList<Image> getAllImages() {
        return allImages == null ? null : allImages.get();
    }

    public void setAllImages(@NonNull ArrayList<Image> images) {
        this.allImages = new WeakReference<>(images);
    }

    private WeakReference<SparseBooleanArray> mCheckedArrayRef;

    public SparseBooleanArray getCheckedArray() {
        return mCheckedArrayRef.get();
    }

    public void setCheckedArray(SparseBooleanArray checkedArray) {
        mCheckedArrayRef = new WeakReference<>(checkedArray);
    }
}
