package com.example.callu.learnbettrui;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;

public class showQuestion extends AppCompatActivity {
    private float rating = 0;
    private int ratingCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myTag", "back to show question" +
                "");
        final Intent intent = new Intent(this, showImage.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question);
        final TextView titleView = (TextView) findViewById(R.id.titleView);
        final TextView desView = (TextView) findViewById(R.id.DesView);
        final Button answersButton = (Button) findViewById(R.id.answersButton);

        //ratingBar.setRating(3);
        //ratingBar.setEnabled(false);
        answersButton.setEnabled(false);

        Log.d("myTag", "Show Question");
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String quuid = b.getString("quuid");
        String declined = b.getString("declined");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference titleref = database.getReference(quuid).child("Title");
        final DatabaseReference desref = database.getReference(quuid).child("Description");
        final DatabaseReference ansRef = database.getReference(quuid).child("pendingAnswer");
        Log.d("myTag", "declined Value " + declined);
        if (declined.equals("true")){
            Log.d("myTag", "declined Value in loop");
           // ansRef.setValue(null);
            ansRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object pendStr = dataSnapshot.getValue().toString();
                    Log.d("myTag", "onDataChange: " + pendStr);
                    if (pendStr != null) {
                        ansRef.removeEventListener(this);
                        dataSnapshot.getRef().setValue("null");

                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

        }else {
            Log.d("myTag", "not declined)");

            ansRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && !dataSnapshot.getValue().equals("null")) {
                        answersButton.setEnabled(true);
                        answersButton.setText("see Answer");


                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    answersButton.setEnabled(false);

                }
            });
        }

        desref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String disDes = dataSnapshot.getValue().toString();
                desView.setText(disDes);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        titleref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String disDes = dataSnapshot.getValue().toString();
                titleView.setText(disDes);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseStorage storage = FirebaseStorage.getInstance();
        String path = "Questions/" + quuid + ".png";
        StorageReference storageRef = storage.getReference();
        final StorageReference QuesstionRef = storage.getReference(path);

        final long ONE_MEGABYTE = 1024 * 1024;
        QuesstionRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSuccess(byte[] bytes) {
                Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                Log.d("myTag", bytes.toString());
                intent.putExtra("image", bytes);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });



        ///this is the bit where I set rating bar
        final DatabaseReference userRef = database.getReference(quuid).child("user");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uid = dataSnapshot.getValue().toString();
                getRating(uid);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void gotoAnswers(View view){

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String quuid = b.getString("quuid");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ansRef = database.getReference(quuid).child("pendingAnswer");
        final Intent intent = new Intent(this, showAnswer.class);
        ansRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ansRef = dataSnapshot.getValue().toString();
                Log.d("myTag", "onDataChange goto answers: "+ ansRef);
                intent.putExtra("ansQuuid", ansRef);
                intent.putExtra("quuid", quuid);
                startActivity(intent);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String qUUID = b.getString("quuid");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("quuid", qUUID);
        startActivity(intent);
    }

    public void getRating(String quuid){
        //setContentView(R.layout.activity_show_question);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ratings = database.getReference("Users").child(quuid).child("Rating");
        Query query = ratings.orderByPriority();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (final DataSnapshot score : dataSnapshot.getChildren()) {
                            rating = rating + Float.parseFloat(score.getValue().toString()) ;
                            ratingCount++;
                        Log.d("myTag", "rating in loop" + rating);

                }

                    rating = rating/ratingCount;
                    ratingBar.setRating(rating);
                    Log.d("myTag", "Return" + rating);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("myTag", "onCancelled: ");

            }

        });
    }

    public void gotoPictures(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String ansQuuid = b.getString("quuid");
        final Intent gotoShowimage = new Intent(showQuestion.this, answerImages.class);
        gotoShowimage.putExtra("ansQuuid", ansQuuid);
        gotoShowimage.putExtra("QA", "Questions");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference countRef = database.getReference(ansQuuid).child("pictures");

        countRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object picCount = dataSnapshot.getValue();
                if (picCount != null){
                    int result = Integer.parseInt(picCount.toString());
                    gotoShowimage.putExtra("picCount", result);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
        //todo: make this go to a grid view with all our pictures
        startActivity(gotoShowimage);
    }


}


