package net.suweya.photopicker.util;

import android.util.SparseBooleanArray;

import java.util.ArrayList;

public class ArrayUtil {

    public static ArrayList<Integer> asKeyList(SparseBooleanArray sparseArray) {
        ArrayList<Integer> arrayList = null;
        if (sparseArray != null && sparseArray.size() > 0) {
            int count = sparseArray.size();
            arrayList = new ArrayList<>(count);
            for (int i = 0; i < count; i++) {
                arrayList.add(sparseArray.keyAt(i));
            }
        }
        return arrayList;
    }
}
