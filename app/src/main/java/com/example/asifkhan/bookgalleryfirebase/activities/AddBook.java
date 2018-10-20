package com.example.asifkhan.bookgalleryfirebase.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.asifkhan.bookgalleryfirebase.R;

public class AddBook extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
    }
}
