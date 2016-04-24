package net.suweya.photopickersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.suweya.photopicker.PhotoPickerFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PhotoPickerFragment fragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment == null) {
            fragment = PhotoPickerFragment.newInstance(true);
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}
