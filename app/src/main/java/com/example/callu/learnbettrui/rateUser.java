package com.example.callu.learnbettrui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class rateUser extends AppCompatActivity {
    public float rating = 0;
    public float stars = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_user);
        final RatingBar ratingBar = findViewById(R.id.ratingBar2);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar rtBar, float rating,boolean fromUser) {
                stars = (int) rating;
            }
        });
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String Uid = b.getString("Quser");

    }





    public void submit(View view){
        setContentView(R.layout.activity_rate_user);
        final RatingBar ratingBar = findViewById(R.id.ratingBar2);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar rtBar, float rating,boolean fromUser) {
             stars = (int) rating;
            }
        });
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String Uid = b.getString("Quser");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth user = FirebaseAuth.getInstance();
        final DatabaseReference rateRef = database.getReference("Users").child(Uid).child("Rating").child(user.getCurrentUser().getUid().toString());

       rateRef.setValue(stars);

    }

}
