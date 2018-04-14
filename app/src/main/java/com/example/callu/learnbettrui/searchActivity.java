package com.example.callu.learnbettrui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class searchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }


    public void search(View view){

        EditText searchField = (EditText) findViewById(R.id.searchField);

        String tag = searchField.getText().toString();
        Log.d("myTag", tag);
        Intent intent = new Intent(this, searchResults.class);
        intent.putExtra("tag", tag);
        startActivity(intent);



    }
}
