package net.suweya.photopicker.util;

import android.support.annotation.Nullable;

import java.util.Arrays;

/**
 * Objects from guava
 */
public class Objects {

    public static int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }
}
