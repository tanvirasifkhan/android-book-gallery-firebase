package com.example.asifkhan.bookgalleryfirebase.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.asifkhan.bookgalleryfirebase.R;
import com.example.asifkhan.bookgalleryfirebase.activities.Update;
import com.example.asifkhan.bookgalleryfirebase.helpers.BookDatabaseHelper;
import com.example.asifkhan.bookgalleryfirebase.helpers.Config;
import com.example.asifkhan.bookgalleryfirebase.models.Book;

import java.util.ArrayList;

public class BookGalleryAdapter extends BaseAdapter {
    private ArrayList<Book> books;
    private Context context;
    private BookDatabaseHelper bookDatabaseHelper;

    public BookGalleryAdapter(ArrayList<Book> books, Context context) {
        this.books = books;
        this.context = context;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView=layoutInflater.inflate(R.layout.custom_gallery_layout,null);
        ImageView coverPhoto,option;
        if(convertView==null){
            coverPhoto=new ImageView(context);
        }
        coverPhoto=(ImageView)convertView.findViewById(R.id.cover);
        option=(ImageView)convertView.findViewById(R.id.option);
        TextView title=(TextView)convertView.findViewById(R.id.title);
        TextView author=(TextView)convertView.findViewById(R.id.author);
        RatingBar rating=(RatingBar)convertView.findViewById(R.id.rating);
        final Book book=books.get(position);
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        rating.setRating(book.getRating());
        bookDatabaseHelper=new BookDatabaseHelper(context);
        Glide.with(context).load(book.getCoverPhotoURL()).crossFade(1000).diskCacheStrategy(DiskCacheStrategy.ALL).into(coverPhoto);
        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(context,v);
                popupMenu.getMenuInflater().inflate(R.menu.option_popup_menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit:
                                Intent intent=new Intent(context,Update.class);
                                intent.putExtra(Config.BOOK_ID,book.getId());
                                intent.putExtra(Config.BOOK_TITLE,book.getTitle());
                                intent.putExtra(Config.BOOK_AUTHOR,book.getAuthor());
                                intent.putExtra(Config.BOOK_RATING,book.getRating());
                                intent.putExtra(Config.BOOK_COVER_PHOTO_URL,book.getCoverPhotoURL());
                                context.startActivity(intent);
                                return true;
                            case R.id.delete:
                                bookDatabaseHelper.remove(context,book);
;                                return true;
                            default:
                                return false;
                        }
                    }
                });
            }
        });
        return convertView;
    }
}
