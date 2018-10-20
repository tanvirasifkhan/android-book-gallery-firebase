package com.example.asifkhan.bookgalleryfirebase.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.asifkhan.bookgalleryfirebase.R;

public class Update extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setSupportActionBar((Toolbar)findViewById(R.id.loader));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
