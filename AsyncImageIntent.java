package com.example.kev.testplication;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import static android.graphics.drawable.Drawable.createFromStream;

public class AsyncImageIntent extends IntentService {
    private Bundle bundle;
    private ResultReceiver rr;
    private String url;
    private int rows, columns;
    public AsyncImageIntent(String name) {
        super(name);
    }
    public AsyncImageIntent() {
        super("DisplayNotification");
    }

    @Override
    public void onCreate(){
        super.onCreate();
        bundle = new Bundle();
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        rr = intent.getParcelableExtra("receiver");
        url = intent.getStringExtra("url");
        rows = intent.getIntExtra("rows", 0);
        columns = intent.getIntExtra("columns", 0);
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "shit");
            BitmapDrawable bitmap = (BitmapDrawable) d;
            bundle.putParcelable("image", bitmap.getBitmap());
            bundle.putSerializable("imagearray", bitmapSplitter(bitmap.getBitmap(),rows,columns));
            intent.putExtra("splits", 16);
            rr.send(1, bundle);
        } catch (MalformedURLException e) {
            rr.send(2,null);
            e.printStackTrace();
        } catch (IOException e) {
            rr.send(3,null);
            e.printStackTrace();
        }
    }
    private Bitmap[][] bitmapSplitter(Bitmap bm, int rows, int columns){
        if(rows == 0 || columns == 0)
            return null;
        Bitmap[][] bmArr = new Bitmap[rows][columns];
        int dWidth = bm.getWidth()/rows;
        int dHeight = bm.getHeight()/columns;
        for(int column = 0; column < columns; column++){
            for(int row = 0; row < rows; row++){
                bmArr[column][row] = Bitmap.createBitmap(bm, row*dWidth, column*dHeight, dWidth, dHeight);
            }
        }
        return bmArr;
    }
}
