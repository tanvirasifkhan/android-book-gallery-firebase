package com.example.asifkhan.bookgalleryfirebase.activities;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.asifkhan.bookgalleryfirebase.R;
import com.example.asifkhan.bookgalleryfirebase.helpers.BookDatabaseHelper;
import com.example.asifkhan.bookgalleryfirebase.helpers.Config;

public class Update extends AppCompatActivity implements View.OnClickListener {
    private EditText titleField,authorField;
    private RatingBar ratingField;
    private ImageView coverPhotoField;
    private Button editBook;
    private Uri coverPhotoURL;
    private BookDatabaseHelper bookDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bookDatabaseHelper=new BookDatabaseHelper(this);
        titleField=(EditText)findViewById(R.id.book_title_field);
        authorField=(EditText)findViewById(R.id.book_author_field);
        ratingField=(RatingBar)findViewById(R.id.rating);
        coverPhotoField=(ImageView)findViewById(R.id.book_cover);
        editBook=(Button)findViewById(R.id.edit_book);
        titleField.setText(getIntent().getExtras().getString(Config.BOOK_TITLE));
        authorField.setText(getIntent().getExtras().getString(Config.BOOK_AUTHOR));
        ratingField.setRating(getIntent().getExtras().getFloat(Config.BOOK_RATING));
        Glide.with(this).load(getIntent().getExtras().getString(Config.BOOK_COVER_PHOTO_URL)).
                crossFade(1000).diskCacheStrategy(DiskCacheStrategy.ALL).into(coverPhotoField);
        editBook.setOnClickListener(this);
        coverPhotoField.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_book:
                update();
                break;
            case R.id.book_cover:
                captureCoverPhoto();
                break;
        }
    }

    // capture book cover photo
    private void captureCoverPhoto() {
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,Config.COVER_PHOTO_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Config.COVER_PHOTO_REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            coverPhotoURL=data.getData();
            coverPhotoField.setImageURI(coverPhotoURL);
        }
    }

    // update book according to id
    private void update() {
        String getTitle=titleField.getText().toString();
        String getAuthor=authorField.getText().toString();
        float getRating=ratingField.getRating();
        boolean isTitleEmpty=titleField.getText().toString().isEmpty();
        boolean isAuthorEmpty=authorField.getText().toString().isEmpty();
        boolean isNoRating=ratingField.getRating()==0;
        boolean isNullPhotoURL=coverPhotoURL==null;
        String book_id=getIntent().getExtras().getString(Config.BOOK_ID);

        if(isTitleEmpty){
            titleField.setError(Config.TITLE_EMPTY_MESSAGE);
        }

        if(isAuthorEmpty){
            authorField.setError(Config.AUTHOR_EMPTY_MESSAGE);
        }

        if(isNoRating){
            Toast.makeText(this, Config.RATING_ZERO_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        if(isNullPhotoURL){
            Toast.makeText(this, Config.IMAGE_URL_NULL_MESSAGE, Toast.LENGTH_SHORT).show();
        }

        if(!isTitleEmpty && !isAuthorEmpty && !isNoRating && !isNullPhotoURL){
            bookDatabaseHelper.edit(getApplicationContext(),book_id,getTitle,getAuthor,getRating,coverPhotoURL);
        }
    }
}
