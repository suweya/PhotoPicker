package net.suweya.photopicker.entity;

import net.suweya.photopicker.util.Objects;

/**
 * 图片
 * Created by suweya on 16/4/9.
 */
public class Image {

    public int position;
    public String path;
    public String name;
    public long time;

    public boolean selected = false;

    public Image(int position, String path, String name, long time) {
        this.position = position;
        this.path = path;
        this.name = name;
        this.time = time;
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
        return "Image{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", time=" + time +
                ", selected=" + selected +
                '}';
    }
}

