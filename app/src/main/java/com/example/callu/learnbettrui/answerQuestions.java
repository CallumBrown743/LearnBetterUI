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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;


public class answerQuestions extends AppCompatActivity {
    final FirebaseAuth user = FirebaseAuth.getInstance();
    private int i = 0;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final int CAMERA_REQUEST = 1888;
    final String ansQuuid = UUID.randomUUID().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_answer_questions);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String qUUID = b.getString("quuid");
        final Intent intent=new Intent(answerQuestions.this,showQuestion3.class);
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
                    String path = "Answers/" + ansQuuid + "/" + i + ".png";
                    StorageReference QuesstionRef = storage.getReference(path);
                    UploadTask uploadTask =QuesstionRef.putBytes(data);
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setEnabled(false);
                    uploadTask.addOnSuccessListener(answerQuestions.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Uri url =taskSnapshot.getDownloadUrl();
                            intent.putExtra("URL",url);
                            submit.setEnabled(true);
                            i++;


                        }
                    });

                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();


                    try {
                        final ProgressBar progressBar = findViewById(R.id.progress_bar);
                        final ImageButton imageButton = findViewById(R.id.imageButton2);

                        final TextView textView = findViewById(R.id.textView);
                        final TextView imageline = findViewById(R.id.textView6);
                        final Button submit = findViewById(R.id.button5);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                        String path = "Answers/" + qUUID + "/" + i + ".png";
                        final StorageReference QuesstionRef = storage.getReference(path);
                        UploadTask uploadTask =QuesstionRef.putBytes(data);
                        progressBar.setVisibility(View.VISIBLE);
                        imageButton.setEnabled(false);
                        submit.setEnabled(false);

                        uploadTask.addOnSuccessListener(answerQuestions.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                progressBar.setVisibility(View.GONE);
                                Uri url =taskSnapshot.getDownloadUrl();
                                intent.putExtra("URL",url);
                                submit.setEnabled(true);
                                imageButton.setEnabled(true);
                                i++;

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


    public void submit(View view) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final EditText ansText = (EditText) findViewById(R.id.editText3);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String qUUID = b.getString("quuid");
        final String tag = b.getString("tag");
            String anstex = ansText.getText().toString();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("myTag", "submit: " + ansQuuid);
            DatabaseReference ansId =database.getReference(ansQuuid);
        Log.d("myTag", "submit: got to here");
            DatabaseReference ansTextData = ansId.child("Text");

            DatabaseReference ansUserId = ansId.child("ansUser");
            final DatabaseReference qTitle = ansId.child("Title");
            DatabaseReference picCount = ansId.child("pictures");
        DatabaseReference questionQuuid = ansId.child("questionQuuid");
        DatabaseReference User = database.getReference("Users").child(user.getUid()).child("Answers").push();
        User.setValue(ansQuuid);
            DatabaseReference questionId = database.getReference(qUUID);
            DatabaseReference pendingAnswer = questionId.child("pendingAnswer");
            pendingAnswer.setValue(ansQuuid);
            questionQuuid.setValue(qUUID);
            ansTextData.setValue(anstex);
            ansUserId.setValue(user.getUid());
            picCount.setValue(i);

        final DatabaseReference ref = database.getReference(qUUID.toString()).child("Title");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String disTitle = dataSnapshot.getValue().toString();
                qTitle.setValue(disTitle);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            startActivity(new Intent(answerQuestions.this, MainActivity.class));


        }




    @Override
    public void startActivity(Intent intent, @Nullable Bundle options) {
        super.startActivity(intent, options);
    }
}


