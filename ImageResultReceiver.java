package com.example.kev.testplication;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class ImageResultReceiver extends ResultReceiver {
    private ImageResultReceiver mReceiver;
    private ImageView imageView;
    private TextView imageCounter;
    private Bitmap[][] bmArr;
    private ArrayList<Bitmap> bmList;
    private boolean recIm, recImArr;
    public ImageResultReceiver(Handler handler, ImageView imageView) {
        super(handler);
        this.imageView = imageView;
    }
    public void setmReceiver(ImageResultReceiver r){
        mReceiver = r;
    }
    public void setImageTextCounter(TextView counter){
        imageCounter = counter;
    }
    private ArrayList<Bitmap> getBitmapList(final Bitmap[][] bArr){
        Iterator<Bitmap> bmIt = new Iterator<Bitmap>() {
            private int jIndex = 0, kIndex = 0;
            private int jLength = bArr.length, kLength = bArr[0].length;
            @Override
            public boolean hasNext() {
                return (jLength + kLength - 1) > (jIndex + kIndex);
            }

            @Override
            public Bitmap next() {
                if(kIndex >= kLength)
                {
                    kIndex = 0;
                    jIndex++;
                }
                return bArr[jIndex][kIndex++];
            }
        };
        ArrayList<Bitmap> bList = new ArrayList<>();
        while (bmIt.hasNext()){
            bList.add(bmIt.next());
        }
        imageCounter.setText("Images: "+bList.size());
        return bList;
    }
    public ArrayList<Bitmap> getBitmapArrayList(){
        return bmList;
    }
    public boolean contentReceived(){
        return (recIm && recImArr);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (mReceiver != null) {
            switch(resultCode){
                case 1:
                    Bitmap bm = resultData.getParcelable("image");
                    imageView.setImageBitmap(bm);
                    recIm = true;
                    Bitmap[][] bArr = (Bitmap[][]) resultData.getSerializable("imagearray");
                    if(bArr != null) {
                        bmArr = bArr;
                        bmList = getBitmapList(bmArr);
                        recImArr = true;
                    }
                    break;
                default:
                    recImArr = false;
                    recIm = false;
                    break;
            }
        }
    }
}
