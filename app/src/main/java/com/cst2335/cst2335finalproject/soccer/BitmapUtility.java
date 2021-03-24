package com.cst2335.cst2335finalproject.soccer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class BitmapUtility {
    public static byte[] getImageBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapImage(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0,image.length);
    }
}
