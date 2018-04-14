package com.example.callu.learnbettrui;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class showAnswer extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Intent gotoShowimage = new Intent(showAnswer.this, answerImages.class);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_answer);
        final TextView ansView = findViewById(R.id.Answertext);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String ansQuuid = b.getString("ansQuuid");
        gotoShowimage.putExtra("ansQuuid", ansQuuid);
        Log.d("myTag", "onCreate: "+ ansQuuid);
        Log.d("myTag", "onCreate: show answer");
        final Button accepted = findViewById(R.id.button7);
        final Button decline = findViewById(R.id.declineButton);


        final DatabaseReference ansRef = database.getReference(ansQuuid).child("Text");

        ansRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object ansText = dataSnapshot.getValue();
                if (ansText != null){
                    ansView.setText(ansText.toString());
                    Log.d("myTag", ":ansText:" + ansText.toString());

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

        final DatabaseReference QquuidRef = database.getReference(ansQuuid).child("questionQuuid");

        QquuidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Object Qquuid = dataSnapshot.getValue();
                if(Qquuid != null){

                    DatabaseReference accRef = database.getReference(Qquuid.toString()).child("acceptedAns");
                    accRef.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("myTag", "Qquuid " + Qquuid);

                            if (dataSnapshot.exists()) {

                                Object acceptedAns = dataSnapshot.getValue();
                                Log.d("myTag", "acceptedAns " + acceptedAns);
                                if (acceptedAns.equals(ansQuuid)) {
                                    accepted.setVisibility(View.GONE);
                                    decline.setVisibility(View.GONE);
                                }

                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {


                        }
                    });

                }



            }



            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


    }

    public void gotoPictures(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String ansQuuid = b.getString("ansQuuid");
       final Intent gotoShowimage = new Intent(showAnswer.this, answerImages.class);
        gotoShowimage.putExtra("ansQuuid", ansQuuid);
        gotoShowimage.putExtra("QA", "answers");
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

    public void accept(View view){
        FirebaseAuth user = FirebaseAuth.getInstance();
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String qUUID = b.getString("quuid");
        String ansQuuid = b.getString("ansQuuid");
        final Intent intent = new Intent(this, showQuestion.class);
        intent.putExtra("quuid", qUUID);
        intent.putExtra("Accepted", "true");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        intent.putExtra("declined", "false");

        startActivity(intent);
        final DatabaseReference decAns = database.getReference(qUUID).child("acceptedAns");
        decAns.setValue(ansQuuid);
    }

    public void decline(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        String qUUID = b.getString("quuid");
        String ansQuuid = b.getString("ansQuuid");
        final Intent intent = new Intent(this, showQuestion.class);
        intent.putExtra("quuid", qUUID);
        intent.putExtra("declined", "true");
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        startActivity(intent);
        final DatabaseReference decAns = database.getReference(qUUID).child("declinedAnswers").push();
        decAns.setValue(ansQuuid);

    }


}
