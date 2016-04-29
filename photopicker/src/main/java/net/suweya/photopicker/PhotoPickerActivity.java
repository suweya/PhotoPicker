package net.suweya.photopicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * PhotoPickerActivity
 */
public class PhotoPickerActivity extends AppCompatActivity {

    private static final String KEY_SHOW_CAMERA = "SHOW_CAMERA";

    public static void start(Context context, boolean showCamera) {
        Intent starter = new Intent(context, PhotoPickerActivity.class);
        starter.putExtra(KEY_SHOW_CAMERA, showCamera);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_photo_picker);

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PhotoPickerFragment fragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            boolean showCamera = getIntent().getBooleanExtra(KEY_SHOW_CAMERA, false);
            fragment = PhotoPickerFragment.newInstance(showCamera);
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
