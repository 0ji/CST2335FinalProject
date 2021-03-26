package com.cst2335.cst2335finalproject.soccer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
/**
 * BitmapUtility
 * This class has two static functions which help converting bytes to Bitmap and Bitmap to bytes.
 * The functionality is used for Article class.
 * */
public class BitmapUtility {
    /**
     * getImageByte
     * @param bitmap is a Bitmap.
     * This function is used for converting a bitmap to bytes
     * @return stream.toByteArray() is a byte array
     */
    public static byte[] getImageBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }
    /**
     * getImageByte
     * @param image is a Bitmap.
     * This function is used for converting a byte array to Bitmap
     * @return BitmapFactory.decodeByteArray() is a bitmap object.
     */
    public static Bitmap getBitmapImage(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0,image.length);
    }
}
