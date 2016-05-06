package net.suweya.photopicker.util;

import android.content.Context;

import net.suweya.photopicker.R;

/**
 * @see StatusBarUtil
 */
public class StatusBarUtil {

    /**
     * A method to find height of the status bar
     * @return status bar height
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result == 0 ? context.getResources().getDimensionPixelOffset(R.dimen.status_bar_height) : result;
    }
}
