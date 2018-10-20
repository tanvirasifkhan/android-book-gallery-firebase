package com.example.asifkhan.bookgalleryfirebase.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.asifkhan.bookgalleryfirebase.R;
import com.example.asifkhan.bookgalleryfirebase.helpers.BookDatabaseHelper;
import com.example.asifkhan.bookgalleryfirebase.helpers.Config;

public class AddBook extends AppCompatActivity implements View.OnClickListener {
    private EditText titleField,authorField;
    private RatingBar ratingField;
    private ImageView coverPhotoField;
    private Button addBook;
    private Uri coverPhotoURL;
    private BookDatabaseHelper bookDatabaseHelper;
    private final static String TITLE_EMPTY_MESSAGE="Title is required !";
    private final static String AUTHOR_EMPTY_MESSAGE="Author is required !";
    private final static String RATING_ZERO_MESSAGE="Give some rating !";
    private final static String IMAGE_URL_NULL_MESSAGE="Choose an image !";
    private final static String BOOK_ADDING_MESSAGE="Adding book ....";
    private final static String BOOK_ADD_SUCCESS_MSG="Book added successfully !";
    private static final String BOOK_ADD_ERROR_MSG = "Error adding book !";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleField=(EditText)findViewById(R.id.book_title_field);
        authorField=(EditText)findViewById(R.id.book_author_field);
        ratingField=(RatingBar)findViewById(R.id.rating);
        coverPhotoField=(ImageView)findViewById(R.id.book_cover);
        addBook=(Button)findViewById(R.id.add);
        addBook.setOnClickListener(this);
        coverPhotoField.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add:
                newBook();
                break;
        }
    }

    // add new book
    private void newBook() {
        String getTitle=titleField.getText().toString();
        String getAuthor=authorField.getText().toString();
        String getRating=String.valueOf(ratingField.getRating());
        boolean isTitleEmpty=titleField.getText().toString().isEmpty();
        boolean isAuthorEmpty=authorField.getText().toString().isEmpty();
        boolean isNoRating=ratingField.getRating()==0;
        boolean isNullPhotoURL=coverPhotoURL==null;

        if(isTitleEmpty){
            titleField.setError(TITLE_EMPTY_MESSAGE);
        }

        if(isAuthorEmpty){
            authorField.setError(AUTHOR_EMPTY_MESSAGE);
        }

        if(isNoRating){
            Toast.makeText(this, RATING_ZERO_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        if(isNullPhotoURL){
            Toast.makeText(this, IMAGE_URL_NULL_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        if(!isTitleEmpty && !isAuthorEmpty && !isNoRating && !isNullPhotoURL){
            bookDatabaseHelper=new BookDatabaseHelper(AddBook.this);
            Config.showToast(BOOK_ADDING_MESSAGE,AddBook.this);
            if(bookDatabaseHelper.add(getTitle,getAuthor,getRating,coverPhotoURL)){
                Config.showToast(BOOK_ADD_SUCCESS_MSG,getApplicationContext());
            }else{
                Toast.makeText(this, BOOK_ADD_ERROR_MSG, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
