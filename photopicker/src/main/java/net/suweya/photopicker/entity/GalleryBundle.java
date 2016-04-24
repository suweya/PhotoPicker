package net.suweya.photopicker.entity;

import java.util.ArrayList;

/**
 * Gallery Bundle
 * Created by suweya on 16/4/9.
 */
public class GalleryBundle {

    public ArrayList<Image> images;

    public ArrayList<Folder> folders;

    public GalleryBundle(ArrayList<Image> images, ArrayList<Folder> folders) {
        this.images = images;
        this.folders = folders;
    }
}
