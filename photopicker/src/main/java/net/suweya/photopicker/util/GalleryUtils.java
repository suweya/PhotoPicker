package net.suweya.photopicker.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import net.suweya.photopicker.entity.Folder;
import net.suweya.photopicker.entity.GalleryBundle;
import net.suweya.photopicker.entity.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Gallery Utils
 */
public class GalleryUtils {

    public static final String ALL_IMAGE = "all-image";

    public static GalleryBundle getAllGalleryImage(@NonNull Context context) {
        final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID };

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String selection = IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3]+"=? ";
        String [] selectionArgs = new String[]{"image/jpeg", "image/png"};
        String sortOrder = IMAGE_PROJECTION[2] + " DESC";

        Cursor cursor = context.getContentResolver().query(uri, IMAGE_PROJECTION, selection, selectionArgs, sortOrder);
        if (cursor != null) {

            ArrayList<Image> images = new ArrayList<>(cursor.getCount());
            LinkedHashMap<String, Folder> folderMap = new LinkedHashMap<>();

            System.out.println("getAllGalleryImage: currentThread = " + Thread.currentThread().getName());

            boolean hasSetAllImageFolder = false;

            try {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));

                    if (!TextUtils.isEmpty(path)) {
                        File file = new File(path);

                        if (file.exists()) {
                            Image image = new Image(cursor.getPosition(), path, name, dateAdded);
                            images.add(image);

                            if (!hasSetAllImageFolder) {
                                Folder folder = new Folder("所有图片", ALL_IMAGE, image);
                                folder.images = images;
                                folderMap.put(ALL_IMAGE, folder);
                                hasSetAllImageFolder = true;
                            }

                            File parentFile = file.getParentFile();
                            if (parentFile != null && parentFile.exists()) {
                                String fp = parentFile.getAbsolutePath();
                                Folder folder = folderMap.get(fp);
                                if (folder == null) {
                                    Folder f = new Folder(parentFile.getName(), fp, image);
                                    ArrayList<Image> imageList = new ArrayList<>();
                                    imageList.add(image);
                                    f.images = imageList;
                                    folderMap.put(fp, f);
                                } else {
                                    folder.images.add(image);
                                }
                            }
                        }
                    }
                }
            } finally {
                cursor.close();
            }

            ArrayList<Folder> folders = new ArrayList<>(folderMap.values());
            return new GalleryBundle(images, folders);
        }

        // cursor is null
        return null;
    }

}
