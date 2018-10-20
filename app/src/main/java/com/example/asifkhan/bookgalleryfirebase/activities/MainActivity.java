package com.example.asifkhan.bookgalleryfirebase.activities;


import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.example.asifkhan.bookgalleryfirebase.R;
import com.example.asifkhan.bookgalleryfirebase.adapters.BookGalleryAdapter;
import com.example.asifkhan.bookgalleryfirebase.models.Book;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GridView gallery;
    private BookGalleryAdapter bookGalleryAdapter;
    private ArrayList<Book> books;
    private FloatingActionButton addBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        gallery=(GridView)findViewById(R.id.book_gallery);
        books=new ArrayList<>();
        bookGalleryAdapter=new BookGalleryAdapter(books,this);
        addBook=(FloatingActionButton)findViewById(R.id.add_book);
        gallery.setAdapter(bookGalleryAdapter);
        addBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_book:
                startActivity(new Intent(MainActivity.this,AddBook.class));
                break;
        }
    }
}
