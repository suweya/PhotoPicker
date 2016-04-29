package net.suweya.photopickersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.suweya.photopicker.PhotoPickerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        PhotoPickerActivity.start(this, true);
    }
}
