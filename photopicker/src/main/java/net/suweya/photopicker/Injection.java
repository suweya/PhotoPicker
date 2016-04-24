package net.suweya.photopicker;

/**
 * Injection
 * Created by suweya on 16/4/9.
 */
public class Injection {

    public static PhotoPickerContract.Model providePhotoPickerModel() {
        return new PhotoPickerModel();
    }

}
