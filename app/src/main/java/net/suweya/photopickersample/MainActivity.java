package net.suweya.photopickersample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.suweya.photopicker.PhotoPickerActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        PhotoPickerActivity.start(this, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PhotoPickerActivity.REQUEST_CODE_PHOTO_PICKER && data != null) {
            ArrayList<String> paths = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_DATA);
            if (paths != null) {
                ((TextView)findViewById(R.id.textView)).setText(paths.toString());
            }
        }
    }
}
