package com.example.callu.learnbettrui;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


public class showImage extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ImageView imageView = (ImageView) findViewById(R.id.imageDisplay);
        setContentView(R.layout.activity_show_image);
        Bundle b = new Bundle();
        b = getIntent().getExtras();
        byte[] bytes;
        bytes = b.getByteArray("image");
        Log.d("myTag", bytes.toString());

        if (bytes != null) {
            Log.d("myTag", "Show Image ");
            Log.d("myTag", bytes.toString());
            Drawable image = new BitmapDrawable(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            imageView.setBackground(image);
        }
    }
}