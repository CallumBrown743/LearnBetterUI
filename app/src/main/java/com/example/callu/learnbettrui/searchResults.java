package com.example.callu.learnbettrui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class searchResults extends AppCompatActivity {
    public String[] quuids = new String[1000];
    public int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search_results);
        final Button q1 = (Button) findViewById(R.id.question1);
        final Button q2 = (Button) findViewById(R.id.question2);
        final Button q3 = (Button) findViewById(R.id.question3);
        final Button q4 = (Button) findViewById(R.id.question4);
        final Button q5 = (Button) findViewById(R.id.question5);
        final TextView noResults = (TextView) findViewById(R.id.noResults);
        final Button next = (Button) findViewById(R.id.Next);
        final Button prev = (Button) findViewById(R.id.Prev);
        final ProgressBar progressBar = findViewById(R.id.LoadingBar);
        super.onCreate(savedInstanceState);


        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final String tag = b.getString("tag").toLowerCase();
        final int qNUM = b.getInt("firstQuestion");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Log.d("myTag", tag);
        DatabaseReference qtags= database.getReference(tag);
        Query query = qtags.orderByPriority().limitToFirst(1000);
        Log.d("myTag", "act 2 qNum: "+ qNUM);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (qNUM != 0){
                    prev.setVisibility(View.VISIBLE);
                }


                q1.setEnabled(false);
                q2.setEnabled(false);
                q3.setEnabled(false);
                q4.setEnabled(false);
                q5.setEnabled(false);
                next.setEnabled(false);



                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (final DataSnapshot quuid : dataSnapshot.getChildren()) {
                        if (quuid.exists()){
                            Log.d("myTag", "onDataChange: after if  but before I make anything a string ");
                           // Log.d("myTag", "onDataChange: after if " +  quuid.getValue().toString());
                            final String quuidStr = quuid.getValue().toString();
                            final DatabaseReference ansRef = database.getReference(quuidStr).child("pendingAnswer");
                            ansRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot pendingAns) {
                                    Log.d("myTag", "onDataChange: PendaAns checking stuff");
                                        if (!pendingAns.exists() ){
                                            Log.d("myTag", "onDataChange: pending ans STR " + pendingAns.getValue());
                                            quuids[i] = quuidStr;
                                            Log.d("myTag1", quuids[i]);
                                            Log.d("myTag", "i: " +i );
                                            i++;
                                            setQuuids(qNUM);

                                        }else if (pendingAns.getValue().equals("null")){
                                            Log.d("myTag", "onDataChange: pending ans STR in else if " + pendingAns.getValue());
                                            quuids[i] = quuidStr;
                                            Log.d("myTag1", quuids[i]);
                                           Log.d("myTag", "i: " +i );
                                            i++;
                                            setQuuids(qNUM);

                                        }else{
                                            Log.d("myTag", "onDataChange: printing the value stored at pendans"+ pendingAns.getValue().toString());
                                        }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }



                            });

                        }else{
                            Log.d("myTag", "onDataChange: in else calling setQuuids");
                           // break;
                        }


                    }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("myTag", "onCancelled: ");

            }

        });





    }


    public void q1Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Log.d("myTag", "q1Click");

        Intent intent = new Intent(this, showQuestion3.class);
        intent.putExtra("quuid", quuids[qNUM]);
        final String tag = b.getString("tag").toLowerCase();
        intent.putExtra("searchTag", tag );
        startActivity(intent);

    }

    public void q2Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, showQuestion3.class);
        intent.putExtra("quuid", quuids[qNUM+1]);
        final String tag = b.getString("tag").toLowerCase();
        intent.putExtra("searchTag", tag );
        startActivity(intent);

    }
    public void q3Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, showQuestion3.class);
        intent.putExtra("quuid", quuids[qNUM+2]);
        final String tag = b.getString("tag").toLowerCase();
        intent.putExtra("searchTag", tag );
        startActivity(intent);

    }

    public void q4Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, showQuestion3.class);
        intent.putExtra("quuid", quuids[qNUM+3]);
        final String tag = b.getString("tag").toLowerCase();
        intent.putExtra("searchTag", tag );
        startActivity(intent);

    }

    public void q5Click(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        Intent intent = new Intent(this, showQuestion3.class);
        intent.putExtra("quuid", quuids[qNUM+4]);
        final String tag = b.getString("tag").toLowerCase();
        intent.putExtra("searchTag", tag );
        startActivity(intent);

    }    public void next(View view){
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        final int qNUM = b.getInt("firstQuestion");
        String tag = b.getString("tag").toLowerCase();
        Intent intent = new Intent(this, searchResults.class);
        intent.putExtra("firstQuestion", qNUM +5 );
        intent.putExtra("tag", tag);
        startActivity(intent);
    }

    public  void prev(View view){
        super.onBackPressed();
    }

    public  void  setQuuids(int qNUM){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final Button q1 = (Button) findViewById(R.id.question1);
        final Button q2 = (Button) findViewById(R.id.question2);
        final Button q3 = (Button) findViewById(R.id.question3);
        final Button q4 = (Button) findViewById(R.id.question4);
        final Button q5 = (Button) findViewById(R.id.question5);
        final TextView noResults = (TextView) findViewById(R.id.noResults);
        final Button next = (Button) findViewById(R.id.Next);
        final Button prev = (Button) findViewById(R.id.Prev);
        final ProgressBar progressBar = findViewById(R.id.LoadingBar);
        Log.d("myTag", "setQuuids: "+ quuids[qNUM]);
        Log.d("myTag", "setQuuids: "+ quuids[qNUM+1]);
        if (quuids[qNUM] != null) {
            final DatabaseReference ref = database.getReference(quuids[qNUM]).child("Title");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    q1.setEnabled(true);
                    String disTitle = dataSnapshot.getValue().toString();
                    Log.d("myTag", disTitle);
                    q1.setText(disTitle);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }else{
            progressBar.setVisibility(View.GONE);
            noResults.setVisibility(View.VISIBLE);
        }


        if (quuids[qNUM+1] != null) {
            final DatabaseReference q2ref = database.getReference(quuids[qNUM+1].toString()).child("Title");
            q2ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    q2.setEnabled(true);
                    q2.setVisibility(View.VISIBLE);

                    String disTitle = dataSnapshot.getValue().toString();
                    Log.d("myTag", disTitle);
                    q2.setText(disTitle);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{

            Log.d("myTag", "onDataChange: else2");
        }


        if (quuids[qNUM+2] != null) {
            final DatabaseReference q3ref = database.getReference(quuids[qNUM+2].toString()).child("Title");
            q3ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    q3.setEnabled(true);
                    String disTitle = dataSnapshot.getValue().toString();
                    Log.d("myTag", disTitle);
                    q3.setText(disTitle);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{

        }
        if (quuids[qNUM+3] != null) {
            final DatabaseReference q4ref = database.getReference(quuids[qNUM+3].toString()).child("Title");
            q4ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    q4.setEnabled(true);
                    String disTitle = dataSnapshot.getValue().toString();
                    Log.d("myTag", disTitle);
                    q4.setText(disTitle);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{

        }
        if (quuids[qNUM+4] != null) {
            final DatabaseReference q5ref = database.getReference(quuids[qNUM+4].toString()).child("Title");
            q5ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    q5.setEnabled(true);
                    String disTitle = dataSnapshot.getValue().toString();
                    Log.d("myTag", disTitle);
                    q5.setText(disTitle);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else{

        }

        if(quuids[qNUM+5] != null){

            next.setEnabled(true);
        }


    }
}
