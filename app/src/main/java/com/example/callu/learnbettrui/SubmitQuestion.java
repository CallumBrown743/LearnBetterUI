package com.example.callu.learnbettrui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;


public class SubmitQuestion extends AppCompatActivity {
    private int picCount = 0;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final int CAMERA_REQUEST = 1888;
    final String qUUID = UUID.randomUUID().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_question);

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final ImageButton camera = findViewById(R.id.imageButton);
        final ImageButton gallery = findViewById(R.id.imageButton2);
        final Intent intent=new Intent(SubmitQuestion.this,showQuestion3.class);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    final ProgressBar progressBar = findViewById(R.id.progress_bar);
                    final Button submit = findViewById(R.id.button5);
                    Bitmap bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] data = baos.toByteArray();
                    String path = "Questions/" + qUUID + "/" + picCount + ".png";
                    StorageReference QuesstionRef = storage.getReference(path);
                    UploadTask uploadTask =QuesstionRef.putBytes(data);
                    progressBar.setVisibility(View.VISIBLE);
                    camera.setEnabled(false);
                    gallery.setEnabled(false);
                    submit.setEnabled(false);
                    uploadTask.addOnSuccessListener(SubmitQuestion.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Uri url =taskSnapshot.getDownloadUrl();
                            intent.putExtra("URL",url);
                            submit.setEnabled(true);
                            camera.setEnabled(true);
                            gallery.setEnabled(true);
                            picCount++;


                        }
                    });

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();


                    try {
                        final ProgressBar progressBar = findViewById(R.id.progress_bar);

                        final Button submit = findViewById(R.id.button5);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        String path = "Questions/" + qUUID + "/" + picCount + ".png";
                        final StorageReference QuesstionRef = storage.getReference(path);
                        UploadTask uploadTask =QuesstionRef.putBytes(data);
                        progressBar.setVisibility(View.VISIBLE);
                        //submit.setEnabled(false);

                        uploadTask.addOnSuccessListener(SubmitQuestion.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                progressBar.setVisibility(View.GONE);
                                Uri url =taskSnapshot.getDownloadUrl();
                                intent.putExtra("URL",url);
                                submit.setEnabled(true);
                                picCount++;

                            }
                        });


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }


        public void takePicture(View view) {
            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePicture, 0); //zero can be replaced with any action code
            //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }


    public void gallery(View view) {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            startActivityForResult(pickPhoto, 1); //one can be replaced with any action co
        }
    public void submit(View view) {
        final EditText title = (EditText) findViewById(R.id.editText);
        final EditText description = (EditText) findViewById(R.id.editText3);
        final TextView tV3 = (TextView) findViewById(R.id.textView3);
        final TextView tV5 = (TextView) findViewById(R.id.textView5);
        final EditText tags = (EditText)findViewById(R.id.editText2);
        final TextView tagsV = (TextView) findViewById(R.id.textView8);
         if (title.getText().toString().isEmpty() ) {
            tV3.setTextColor(Color.RED);

        }
        else if ( tags.getText().toString().isEmpty() ) {
             tagsV.setTextColor(Color.RED);
        }


        else{
            Intent intent = new Intent(SubmitQuestion.this,showQuestion3.class);
             intent.putExtra("tile", title.toString());
             String questionDis =description.getText().toString();
             if ( !questionDis.isEmpty() ) {
                 Log.d("myTag", questionDis);
                 intent.putExtra("description", questionDis);
             }
            String tagsStream = tags.getText().toString().toLowerCase();


             String[] tagsArray = tagsStream.split("\\s+");


             String questionTitle =title.getText().toString();
             Log.d("myTag", questionTitle);
             intent.putExtra("tile", questionTitle);
             FirebaseDatabase database = FirebaseDatabase.getInstance();
             DatabaseReference questionId = database.getReference(qUUID);
             DatabaseReference titleid = questionId.child("Title");
             DatabaseReference disid = questionId.child("Description");
             DatabaseReference Userid = questionId.child("user");
             DatabaseReference Pictures = questionId.child("pictures");
             Pictures.setValue(picCount);

             for(int i = 0; i < tagsArray.length; i++)
             {
                 Log.d("myTag", tagsArray[i]);
                 DatabaseReference tag = database.getReference(tagsArray[i]);
                 DatabaseReference tagId = tag.push();
                 tagId.setValue(qUUID);
             }
             titleid.setValue(questionTitle);
             disid.setValue(questionDis);
             FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

             Userid.setValue(user.getUid());
             intent.putExtra("quuid",qUUID);
             startActivity(intent);
             DatabaseReference User = database.getReference("Users").child(user.getUid()).child("Questions").push();
             User.setValue(qUUID);



        }


    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
    }
}


