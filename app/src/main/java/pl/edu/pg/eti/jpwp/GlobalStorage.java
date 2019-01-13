package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static java.lang.Math.round;

public class GlobalStorage {
    private static int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private static int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private static double XScale = screenWidth / 720;
    private static double YScale = screenHeight / 1280;

    public static final int HAND_FOREHEAD_X = (int)round(XScale * 388);
    public static final int HAND_FOREHEAD_Y = (int)round(YScale * 169);
    public static final int HAND_CHIN_X = (int)round(XScale * 395);
    public static final int HAND_CHIN_Y = (int)round(YScale * 747);

    public static final int RING_FOREHEAD_X = (int)round(XScale * 360);
    public static final int RING_FOREHEAD_Y = (int)round(YScale * 300);
    public static final int RING_CHIN_X = (int)round(XScale * 360);
    public static final int RING_CHIN_Y = (int)round(YScale * 600);

    public static final int HAND_DISTANCE = 120;
    public static final int HAND_FLIP_Y = (RING_FOREHEAD_Y + RING_CHIN_Y)/2;

    public static final int HEAD_MIN_Y = (int)round(YScale * 265);
    public static final int HEAD_MAX_Y = (int)round(YScale * 328);

    public static Bitmap imgBackground;
    public static Bitmap imgBody;
    public static Bitmap imgHead;
    public static Bitmap imgHandUp;
    public static Bitmap imgHandDown;
    public static Bitmap imgIconOk;

    public GlobalStorage(Context context){

        System.out.println("HEAD MAX Y: " + HEAD_MAX_Y);

        imgBackground = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.floor),
                screenWidth, screenHeight, true);

        imgBody = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.body),
                (int)round(XScale * 540),
                (int)round(YScale * 723),
                true
        );

        imgHead = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.head),
                (int)round(XScale * 283),
                (int)round(YScale * 352),
                true);

        imgHandDown = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.hand_down),
                (int)round(XScale * 240),
                (int)round(XScale * 240),
                true
        );

        imgHandUp = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.hand_up),
                (int)round(XScale * 240),
                (int)round(XScale * 240),
                true
        );

        imgIconOk = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_ok),
                (int)round(XScale * 128),
                (int)round(XScale * 128),
                true
        );


    }
}
