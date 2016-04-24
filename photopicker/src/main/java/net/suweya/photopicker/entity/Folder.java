package net.suweya.photopicker.entity;

import net.suweya.photopicker.util.Objects;

import java.util.ArrayList;

/**
 * 目录
 * Created by suweya on 16/4/9.
 */
public class Folder {

    public String name;
    public String path;
    public Image cover;
    public ArrayList<Image> images;

    public Folder(String name, String path, Image cover) {
        this.name = name;
        this.path = path;
        this.cover = cover;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;
        return image.path.equalsIgnoreCase(path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(path);
    }

    @Override
    public String toString() {
        return "Folder{" +
                "name='" + name + '\'' +
                '}';
    }
}