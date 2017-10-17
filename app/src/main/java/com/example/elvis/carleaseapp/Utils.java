package com.example.elvis.carleaseapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by LucyZhao on 2017/10/16.
 */

public class Utils {
    /**
     * uses a test image
     * todo change this
     * @return
     */
    public static byte[] imgToByteArray(Bitmap b) {
        //Bitmap b = BitmapFactory.decodeResource(context.getResources(),R.drawable.test_img);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap byteArrayToImage(byte[] imgBytes) {
        Bitmap b = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length); //Convert bytearray to bitmap
        return b;
    }
}
