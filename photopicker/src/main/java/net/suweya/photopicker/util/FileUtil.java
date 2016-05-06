package net.suweya.photopicker.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * FileUtil
 */
public class FileUtil {

    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String JPEG_NAME = "JPEG_";
    private static final String SUFFIX = ".jpg";

    public static File createTakePhotoImageFile(File storageDir) {
        File image = null;
        try {
            String timeStamp = new SimpleDateFormat(DATE_FORMAT, Locale.CHINA).format(new Date());
            String imageFileName = JPEG_NAME + timeStamp;
            image = File.createTempFile(imageFileName, SUFFIX, storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
