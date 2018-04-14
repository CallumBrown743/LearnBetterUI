package com.example.callu.learnbettrui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class userAnswers extends AppCompatActivity {
    final String[] quuids = new String[1000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference qtags= database.getReference("Users").child(user.getUid()).child("Answers");
        Query query = qtags.orderByPriority().limitToFirst(1000);
        Log.d("myTag", "showAnswers "+ qNUM);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Button q1 = (Button) findViewById(R.id.question1);
                final Button q2 = (Button) findViewById(R.id.question2);
                final Button q3 = (Button) findViewById(R.id.question3);
                final Button q4 = (Button) findViewById(R.id.question4);
                final Button q5 = (Button) findViewById(R.id.question5);
                final TextView noResults = (TextView) findViewById(R.id.noResults);
                final Button next = (Button) findViewById(R.id.Next);
                final Button prev = (Button) findViewById(R.id.Prev);
                if (qNUM != 0){
                    prev.setVisibility(View.VISIBLE);
                }


                int i=0;
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot quuid : dataSnapshot.getChildren()) {
                        if (quuid.exists()){
                            quuids[i] = quuid.getValue().toString();
                            Log.d("myTag", quuids[i]);
                            Log.d("myTag", "i value" + i);
                            i++;
                        }else{
                            break;
                        }

                    }

                    final DatabaseReference ref = database.getReference(quuids[qNUM].toString()).child("Title");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String disTitle = dataSnapshot.getValue().toString();
                            Log.d("myTag", disTitle);
                            q1.setText("Answer to: " + disTitle);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    if (quuids[qNUM+1] != null) {
                        final DatabaseReference q2ref = database.getReference(quuids[qNUM+1].toString()).child("Title");
                        q2ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String disTitle = dataSnapshot.getValue().toString();
                                Log.d("myTag", disTitle);
                                q2.setText("Answer to: " + disTitle);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        q2.setVisibility(View.INVISIBLE);
                    }


                    if (quuids[qNUM+2] != null) {
                        final DatabaseReference q3ref = database.getReference(quuids[qNUM+2].toString()).child("Title");
                        q3ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String disTitle = dataSnapshot.getValue().toString();
                                Log.d("myTag", disTitle);
                                q3.setText("Answer to: " + disTitle);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        q3.setVisibility(View.INVISIBLE);
                    }
                    if (quuids[qNUM+3] != null) {
                        final DatabaseReference q4ref = database.getReference(quuids[qNUM+3].toString()).child("Title");
                        q4ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String disTitle = dataSnapshot.getValue().toString();
                                Log.d("myTag", disTitle);
                                q4.setText("Answer to: " + disTitle);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        q4.setVisibility(View.INVISIBLE);
                    }
                    if (quuids[qNUM+4] != null) {
                        final DatabaseReference q5ref = database.getReference(quuids[qNUM+4].toString()).child("Title");
                        q5ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String disTitle = dataSnapshot.getValue().toString();
                                Log.d("myTag", disTitle);
                                q5.setText("Answer to: " + disTitle);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        q5.setVisibility(View.INVISIBLE);
                    }

                    if(quuids[qNUM+5] != null){
                        next.setVisibility(View.VISIBLE);
                    }




                }else{
                    q1.setVisibility(View.INVISIBLE);
                    q2.setVisibility(View.INVISIBLE);
                    q3.setVisibility(View.INVISIBLE);
                    q4.setVisibility(View.INVISIBLE);
                    q5.setVisibility(View.INVISIBLE);
                    noResults.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }



        });


    }


    public void q1Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Log.d("myTag", "q1Click");
        Intent intent = new Intent(this, showUserAnswer.class);
        intent.putExtra("ansQuuid", quuids[qNUM]);
        intent.putExtra("declined", "false");
        startActivity(intent);

    }

    public void q2Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, showUserAnswer.class);
        intent.putExtra("ansQuuid", quuids[qNUM+1]);
        intent.putExtra("declined", "false");
        startActivity(intent);

    }
    public void q3Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, showUserAnswer.class);
        intent.putExtra("ansQuuid", quuids[qNUM+2]);
        intent.putExtra("declined", "false");
        startActivity(intent);

    }

    public void q4Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, showUserAnswer.class);
        intent.putExtra("ansQuuid", quuids[qNUM+3]);
        intent.putExtra("declined", "false");
        startActivity(intent);

    }

    public void q5Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, showUserAnswer.class);
        intent.putExtra("ansQuuid", quuids[qNUM+4]);
        intent.putExtra("declined", "false");
        startActivity(intent);

    }    public void next(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, userAnswers.class);
        intent.putExtra("firstQuestion", qNUM +5 );
        intent.putExtra("declined", "false");
        startActivity(intent);
    }

    public  void prev(View view){
        super.onBackPressed();
    }




}
