package com.doctor.finder.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.doctor.finder.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSavedActivity(View view) {
        Intent intent = new Intent(this,SavedDoctorActivity.class);
        startActivity(intent);
    }

    public void startSearchtActivity(View view) {
        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);
    }
}
