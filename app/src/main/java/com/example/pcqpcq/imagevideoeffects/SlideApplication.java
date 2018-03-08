package com.example.pcqpcq.imagevideoeffects;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Parcelable;

import java.util.ArrayList;

public class SlideApplication extends Application {
    public static String BGM_PATH = "";
    public static int SLIDE_TIME = 2;
    public static int SLIDE_EFFECT = SlideShow.NONE;
    public static int BIT_RATE = 2000 * 1024;
    public static int FRAME_PER_SEC = 30;
    public static ArrayList<Bitmap> bitmapList = new ArrayList<>();
    public static int overlayEffect=0;
    public  static Parcelable[] images;
    public  static String deleteFolderPath;


}