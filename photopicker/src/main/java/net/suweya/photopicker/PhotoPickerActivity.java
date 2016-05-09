package net.suweya.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * PhotoPickerActivity
 */
public class PhotoPickerActivity extends AppCompatActivity {

    private static final String KEY_SHOW_CAMERA = "SHOW_CAMERA";
    public static final String KEY_SELECTED_DATA = "SELECTED_DATA";
    public static final String KEY_MAX_IMAGE_NUM = "MAX_IMAGE_NUM";
    public static final int REQUEST_CODE_PHOTO_PICKER = 10;
    public static final int DEFAULT_MAX_IMAGE_NUM = 9;

    public static void start(Activity context, boolean showCamera) {
        Intent starter = new Intent(context, PhotoPickerActivity.class);
        starter.putExtra(KEY_SHOW_CAMERA, showCamera);
        context.startActivityForResult(starter, REQUEST_CODE_PHOTO_PICKER);
    }

    public static void start(Activity context, boolean showCamera, int maxImageNum) {
        Intent starter = new Intent(context, PhotoPickerActivity.class);
        starter.putExtra(KEY_SHOW_CAMERA, showCamera);
        starter.putExtra(KEY_MAX_IMAGE_NUM, maxImageNum);
        context.startActivityForResult(starter, REQUEST_CODE_PHOTO_PICKER);
    }

    private Button mBtnSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_photo_picker);

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mBtnSend = (Button) findViewById(R.id.btn_send);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        PhotoPickerFragment fragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            boolean showCamera = getIntent().getBooleanExtra(KEY_SHOW_CAMERA, false);
            int maxImageNum = getIntent().getIntExtra(KEY_MAX_IMAGE_NUM, DEFAULT_MAX_IMAGE_NUM);
            fragment = PhotoPickerFragment.newInstance(showCamera, maxImageNum);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }

        final PhotoPickerFragment finalFragment = fragment;
        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (finalFragment != null) {
                    Intent intent = new Intent();
                    intent.putExtra(KEY_SELECTED_DATA, finalFragment.getSelectedImagePath());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void finishWithCamera(String path) {
        Intent intent = new Intent();
        ArrayList<String> paths = new ArrayList<>(1);
        paths.add(path);
        intent.putExtra(KEY_SELECTED_DATA, paths);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void modifySendButtonNum(int num, int max) {
        boolean enable = num != 0;
        mBtnSend.setEnabled(enable);
        mBtnSend.setText(enable ? String.format(getString(R.string.send_count), num, max) : getString(R.string.send));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
