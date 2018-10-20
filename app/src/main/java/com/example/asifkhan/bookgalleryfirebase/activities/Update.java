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

import com.example.asifkhan.bookgalleryfirebase.R;
import com.example.asifkhan.bookgalleryfirebase.helpers.BookDatabaseHelper;

public class Update extends AppCompatActivity implements View.OnClickListener {
    private EditText titleField,authorField;
    private RatingBar ratingField;
    private ImageView coverPhotoField;
    private Button editBook;
    private Uri coverPhotoURL;
    private BookDatabaseHelper bookDatabaseHelper;

    private final static String TITLE_EMPTY_MESSAGE="Title is required !";
    private final static String AUTHOR_EMPTY_MESSAGE="Author is required !";
    private final static String RATING_ZERO_MESSAGE="Give some rating !";
    private final static String IMAGE_URL_NULL_MESSAGE="Choose an image !";
    private final static String BOOK_ADD_SUCCESS_MSG="Book added successfully !";


    private static final int COVER_PHOTO_REQUEST_CODE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setSupportActionBar((Toolbar)findViewById(R.id.loader));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        titleField=(EditText)findViewById(R.id.book_title_field);
        authorField=(EditText)findViewById(R.id.book_author_field);
        ratingField=(RatingBar)findViewById(R.id.rating);
        coverPhotoField=(ImageView)findViewById(R.id.book_cover);
        editBook=(Button)findViewById(R.id.edit_book);
        editBook.setOnClickListener(this);
        coverPhotoField.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_book:
                break;
        }
    }
}
