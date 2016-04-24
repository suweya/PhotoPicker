package net.suweya.photopicker.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import net.suweya.photopicker.R;

import static net.suweya.photopicker.preview.ImagePreviewFragment.CURRENT_ITEM;

/**
 * ImagePreviewActivity
 * Created by suweya on 16/4/10.
 */
public class ImagePreviewActivity extends AppCompatActivity {

    private static final int DEFAULT_VALUE = 0;

    public static void start(Context context, int item) {
        Intent starter = new Intent(context, ImagePreviewActivity.class);
        starter.putExtra(CURRENT_ITEM, item);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_preview);

        ImagePreviewFragment fragment = (ImagePreviewFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            int item = getIntent().getIntExtra(CURRENT_ITEM, DEFAULT_VALUE);
            fragment = ImagePreviewFragment.newInstance(item);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}
