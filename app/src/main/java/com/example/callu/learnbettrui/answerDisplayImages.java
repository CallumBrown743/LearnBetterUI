package com.example.callu.learnbettrui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class answerDisplayImages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_display_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String quuid = b.getString("ansQuuid");
        final String QA = b.getString("QA");
        Log.d("myTag", "onCreate: QA value " + QA);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference countRef = database.getReference(quuid).child("pictures");


    }
}
