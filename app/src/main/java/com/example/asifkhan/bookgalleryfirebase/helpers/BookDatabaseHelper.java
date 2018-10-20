package com.example.asifkhan.bookgalleryfirebase.helpers;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.GridView;


import com.example.asifkhan.bookgalleryfirebase.adapters.BookGalleryAdapter;
import com.example.asifkhan.bookgalleryfirebase.models.Book;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
    private final static String DATABASE_REFERENCE="books";
    private final static String STORAGE_PATH="cover_photo/";
    private final static String BOOK_ADDING_MESSAGE="Adding book ....";
    private final static String BOOK_UPDATING_MESSAGE="Updating book ....";

    public BookDatabaseHelper(Context context){
        this.context=context;
    }

    // add new book into the firebase database
    public boolean add(final Context context,final String title, final String author, final String rating, final Uri coverPhotoURL){
        databaseReference=FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE);
        Config.showToast(BOOK_ADDING_MESSAGE,context);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final String uniqueKey=databaseReference.push().getKey();
                storageReference=FirebaseStorage.getInstance().getReference().child(uniqueKey).child(STORAGE_PATH+coverPhotoURL.getLastPathSegment());
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
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    // check if there is any book in the database
    public boolean anyBookExists(DataSnapshot dataSnapshot){
        return (dataSnapshot.getChildrenCount()>0)?true:false;
    }

    // get all the books from the database
    public void all(final AVLoadingIndicatorView loader, final ArrayList<Book> books, final Context context, final GridView bookGallery){
        databaseReference=FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE);
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
                }else{
                    bookGallery.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // edit book according to the id
    public boolean edit(final Context context,final String id,final String title, final String author, final String rating, final Uri coverPhotoURL){
        databaseReference=FirebaseDatabase.getInstance().getReference(DATABASE_REFERENCE);
        Config.showToast(BOOK_UPDATING_MESSAGE,context);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                storageReference=FirebaseStorage.getInstance().getReference().child(id).child(STORAGE_PATH+coverPhotoURL.getLastPathSegment());
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
                            Book book=new Book(id,title,author,rating,downloadURi.toString());
                            databaseReference.setValue(book);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }
}
