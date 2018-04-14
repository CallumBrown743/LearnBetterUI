package com.example.callu.learnbettrui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class answerImages extends AppCompatActivity {
    public int photoGetLoop = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_images);
        final TextView urlView = findViewById(R.id.urlView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String quuid = b.getString("ansQuuid");
        final String QA = b.getString("QA");
        Log.d("myTag", "onCreate: QA value " + QA);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference countRef = database.getReference(quuid).child("pictures");

        countRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object picCountStr = dataSnapshot.getValue();
                if (picCountStr != null){
                   final int picCount = Integer.parseInt(picCountStr.toString());
                    Log.d("myTag", "onCreate: pic Count " + Integer.toString(picCount));

                    while (photoGetLoop <= picCount){

                        FirebaseStorage ansImages = FirebaseStorage.getInstance();
                        String strPhotoLoop = Integer.toString(photoGetLoop);
                        String path = QA + "/" + quuid + "/"+ strPhotoLoop +".png";
                        Log.d("myTag","path"   +  path);
                        StorageReference storageRef = ansImages.getReference();
                        final StorageReference QuesstionRef = ansImages.getReference(path);
                        final long ONE_MEGABYTE = 1024 * 1024;

                        QuesstionRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                urlView.append("image: " + uri);

                                Linkify.addLinks(urlView, Linkify.WEB_URLS);
                                urlView.append("\n");



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                        photoGetLoop++;

                    }
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });




    }


}
