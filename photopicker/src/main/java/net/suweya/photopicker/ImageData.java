package net.suweya.photopicker;

import android.support.annotation.Nullable;

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

    @Nullable
    public ArrayList<Image> getImages() {
        return images == null ? null : images.get();
    }

    public void setImages(ArrayList<Image> images) {
        this.images = new WeakReference<>(images);
    }
}
