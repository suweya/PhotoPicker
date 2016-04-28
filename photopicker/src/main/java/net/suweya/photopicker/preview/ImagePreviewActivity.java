package net.suweya.photopicker.preview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.suweya.photopicker.R;

import java.util.ArrayList;

import static net.suweya.photopicker.preview.ImagePreviewFragment.CURRENT_ITEM;

/**
 * ImagePreviewActivity
 * Created by suweya on 16/4/10.
 */
public class ImagePreviewActivity extends AppCompatActivity {

    private static final int DEFAULT_VALUE = 0;
    public static final String KEY_CHECKED_POS_ARRAY = "CHECKED_POS_ARRAY";

    public static void start(Fragment fragment, int item) {
        Intent starter = new Intent(fragment.getContext(), ImagePreviewActivity.class);
        starter.putExtra(CURRENT_ITEM, item);
        fragment.startActivityForResult(starter, );
    }

    public static void start(Context context, ArrayList<Integer> array) {
        Intent starter = new Intent(context, ImagePreviewActivity.class);
        starter.putExtra(KEY_CHECKED_POS_ARRAY, array);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_image_preview);

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ImagePreviewFragment fragment = (ImagePreviewFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            ArrayList<Integer> list = getIntent().getIntegerArrayListExtra(KEY_CHECKED_POS_ARRAY);
            if (list == null) {
                int item = getIntent().getIntExtra(CURRENT_ITEM, DEFAULT_VALUE);
                fragment = ImagePreviewFragment.newInstance(item);
            } else {
                fragment = ImagePreviewFragment.newInstance(list);
            }
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
