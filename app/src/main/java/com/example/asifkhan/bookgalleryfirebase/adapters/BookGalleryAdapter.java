package com.example.asifkhan.bookgalleryfirebase.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.asifkhan.bookgalleryfirebase.R;
import com.example.asifkhan.bookgalleryfirebase.models.Book;

import java.util.ArrayList;

public class BookGalleryAdapter extends BaseAdapter {
    private ArrayList<Book> books;
    private Context context;

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
        Book book=books.get(position);
        title.setText(book.getTitle());
        author.setText(book.getAuthor());
        rating.setRating(Float.parseFloat(book.getRating()));
        Glide.with(context).load(book.getCoverPhotoURL()).crossFade(1000).diskCacheStrategy(DiskCacheStrategy.ALL).into(coverPhoto);
        return convertView;
    }
}
