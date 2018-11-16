package com.example.kev.testplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button bConfirm, bNext;
    private EditText etUrl, etR, etC;
    private TextView tvCount;
    private ImageView imDisplay;
    private ImageResultReceiver resultReceiver;
    private ArrayList<Bitmap> bitmapList;
    private boolean initialized;
    private int count = 0, maxCount;
    private Intent imageIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bConfirm = findViewById(R.id.b_conf);
        bNext = findViewById(R.id.b_next);
        etUrl = findViewById(R.id.et_url);
        etR = findViewById(R.id.et_r);
        etC = findViewById(R.id.et_c);
        tvCount = findViewById(R.id.tv_count);
        imDisplay = findViewById(R.id.im_v);

        resultReceiver = new ImageResultReceiver(new Handler(), imDisplay);
        resultReceiver.setmReceiver(resultReceiver);
        resultReceiver.setImageTextCounter(tvCount);
    }
    public void confirmOnClick(View v){
        if(etR.getText().toString().equals("") || etC.getText().toString().equals("")){
            Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
        }else {
            initialized = false;
            count = 0;
            String url = etUrl.getText().toString();
            imageIntent = new Intent(this, AsyncImageIntent.class);
            imageIntent.putExtra("receiver", resultReceiver);
            imageIntent.putExtra("url", url);
            imageIntent.putExtra("rows", Integer.parseInt(etR.getText().toString()));
            imageIntent.putExtra("columns", Integer.parseInt(etC.getText().toString()));
            startService(imageIntent);
        }
    }
    public void nextOnClick(View v){
        if(resultReceiver.contentReceived() && !initialized){
            stopService(imageIntent);
            Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show();
            bitmapList = resultReceiver.getBitmapArrayList();
            initialized = true;
            maxCount = bitmapList.size();
        }
        if(initialized){
            if(count==maxCount)
                count = 0;
            imDisplay.setImageBitmap(bitmapList.get(count));
            count++;
            Toast.makeText(this, "count:" +count + "<" + maxCount, Toast.LENGTH_SHORT).show();
        }
    }
}
