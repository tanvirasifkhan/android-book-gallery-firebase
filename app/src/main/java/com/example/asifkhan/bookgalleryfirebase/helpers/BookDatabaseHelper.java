package com.example.asifkhan.bookgalleryfirebase.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.asifkhan.bookgalleryfirebase.activities.MainActivity;
import com.example.asifkhan.bookgalleryfirebase.adapters.BookGalleryAdapter;
import com.example.asifkhan.bookgalleryfirebase.models.Book;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

public class BookDatabaseHelper {
    private Context context;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public BookDatabaseHelper(Context context){
        this.context=context;
    }

    // add new book into the firebase database
    public void add(final Context context,final String title, final String author, final float rating, final Uri coverPhotoURL){
        databaseReference=FirebaseDatabase.getInstance().getReference(Config.DATABASE_REFERENCE);
        Config.showToast(Config.BOOK_ADDING_MESSAGE,context);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final String uniqueKey=databaseReference.push().getKey();
                storageReference=FirebaseStorage.getInstance().getReference().child(uniqueKey).child(Config.STORAGE_PATH+coverPhotoURL.getLastPathSegment());
                StorageTask storageTask=storageReference.putFile(coverPhotoURL);
                Task<Uri> uriTask=storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> taskSnapshot) throws Exception {
                        if(!taskSnapshot.isSuccessful()){
                            throw taskSnapshot.getException();
                        }
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadURi=task.getResult();
                            Book book=new Book(title,author,rating,downloadURi.toString());
                            databaseReference.child(uniqueKey).setValue(book);
                            Config.showToast(Config.BOOK_ADD_SUCCESS_MSG,context);
                        }
                        Intent intent=new Intent(context,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // check if there is any book in the database
    public boolean anyBookExists(DataSnapshot dataSnapshot){
        return (dataSnapshot.getChildrenCount()>0)?true:false;
    }

    // get all the books from the database
    public void all(final TextView checkInternet, final TextView check, final AVLoadingIndicatorView loader, final ArrayList<Book> books, final Context context, final GridView bookGallery){
        databaseReference=FirebaseDatabase.getInstance().getReference(Config.DATABASE_REFERENCE);
        loader.setVisibility(View.VISIBLE);
        if(Config.isNetworkAvailable(context)){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(anyBookExists(dataSnapshot)){
                        books.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Book book=snapshot.getValue(Book.class);
                            book.setId(snapshot.getKey());
                            books.add(book);
                        }
                        BookGalleryAdapter bookGalleryAdapter=new BookGalleryAdapter(books,context);
                        bookGallery.setAdapter(bookGalleryAdapter);
                        loader.setVisibility(View.GONE);
                        check.setVisibility(View.GONE);
                    }else{
                        bookGallery.setVisibility(View.GONE);
                        check.setVisibility(View.VISIBLE);
                        loader.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
            checkInternet.setVisibility(View.GONE);
        }else {
            checkInternet.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
            bookGallery.setVisibility(View.GONE);
            check.setVisibility(View.GONE);
        }
    }

    // edit book according to the id
    public void edit(final Context context,final String id,final String title, final String author, final float rating, final Uri coverPhotoURL){
        databaseReference=FirebaseDatabase.getInstance().getReference(Config.DATABASE_REFERENCE).child(id);
        Config.showToast(Config.BOOK_UPDATING_MESSAGE,context);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                storageReference=FirebaseStorage.getInstance().getReference().child(id).child(Config.STORAGE_PATH+coverPhotoURL.getLastPathSegment());
                StorageTask storageTask=storageReference.putFile(coverPhotoURL);
                Task<Uri> uriTask=storageTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> taskSnapshot) throws Exception {
                        if(!taskSnapshot.isSuccessful()){
                            throw taskSnapshot.getException();
                        }
                        return storageReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadURi=task.getResult();
                            Book book=new Book(title,author,rating,downloadURi.toString());
                            databaseReference.setValue(book);
                            Config.showToast(Config.BOOK_UPDATE_SUCCESS_MSG,context);
                        }
                        Intent intent=new Intent(context,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // remove book from the database
    public boolean remove(final Context context, final Book book){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(Config.BOOK_REMOVE_DIALOG_TITLE);
        builder.setMessage(Config.BOOK_REMOVE_DIALOG_MSG);
        builder.setPositiveButton(Config.BOOK_REMOVE_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                databaseReference=FirebaseDatabase.getInstance().getReference(Config.DATABASE_REFERENCE).child(book.getId());
                storageReference=FirebaseStorage.getInstance().getReferenceFromUrl(book.getCoverPhotoURL());
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        databaseReference.removeValue();
                        Config.showToast(Config.BOOK_REMOVE_SUCCESS_MSG,context);
                    }
                });
            }
        }).setNegativeButton(Config.BOOK_REMOVE_CANCEL_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
        return true;
    }
}
