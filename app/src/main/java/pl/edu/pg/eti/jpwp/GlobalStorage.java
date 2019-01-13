package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static java.lang.Math.round;

public class GlobalStorage {
    public static final int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    public static final int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public static final float XScale = screenWidth / 720;
    public static final float YScale = screenHeight / 1280;

    public static final int HAND_FOREHEAD_X = round(XScale * 388);
    public static final int HAND_FOREHEAD_Y = round(YScale * 169);
    public static final int HAND_CHIN_X = round(XScale * 395);
    public static final int HAND_CHIN_Y = round(YScale * 747);

    public static final int RING_FOREHEAD_X = round(XScale * 360);
    public static final int RING_FOREHEAD_Y = round(YScale * 300);
    public static final int RING_CHIN_X = round(XScale * 360);
    public static final int RING_CHIN_Y = round(YScale * 600);

    public static final int RING_CHEST_X = round(screenWidth * 0.5f);
    public static final int RING_CHEST_Y = round(screenHeight * 0.76f);

    public static final int MOUTH_X = round(screenWidth * 0.5f);
    public static final int MOUTH_Y = round(screenHeight * 0.42f);

    public static final int HAND_DISTANCE = 120;
    public static final int HAND_FLIP_Y = (RING_FOREHEAD_Y + RING_CHIN_Y)/2;

    public static final int HEAD_MIN_Y = round(YScale * 265);
    public static final int HEAD_MAX_Y = round(YScale * 328);

    public static final float TEXT_SIZE = YScale * 75;

    public static Bitmap imgBackground;
    public static Bitmap imgManekin;
    public static Bitmap imgBody;
    public static Bitmap imgHead;
    public static Bitmap imgHandUp;
    public static Bitmap imgHandDown;
    public static Bitmap imgIconOk;
    public static Bitmap imgWarning;
    public static Bitmap imgClock;
    public static Bitmap imgMenuIcon;
    public static Bitmap imgMenuButton;
    public static Bitmap imgMenuButtonPressed;

    public GlobalStorage(Context context){

        System.out.println("HEAD MAX Y: " + HEAD_MAX_Y);

        imgBackground = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.floor),
                screenWidth,
                screenHeight,
                true
        );

        imgManekin = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.manekin),
                round(XScale * 540),
                round(YScale * 1023),
                true
        );

        imgBody = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.body),
                round(XScale * 540),
                round(YScale * 723),
                true
        );

        imgHead = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.head),
                round(XScale * 283),
                round(YScale * 352),
                true);

        imgHandDown = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.hand_down),
                round(XScale * 240),
                round(XScale * 240),
                true
        );

        imgHandUp = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.hand_up),
                round(XScale * 240),
                round(XScale * 240),
                true
        );

        imgWarning = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.warning),
                round(XScale * 128),
                round(XScale * 128),
                true
        );

        imgClock = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.clock),
                round(XScale * 128),
                round(XScale * 128),
                true
        );

        imgIconOk = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.icon_ok),
                round(XScale * 128),
                round(XScale * 128),
                true
        );

        imgMenuIcon = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.menu_icon),
                round(XScale * 100),
                round(XScale * 100),
                true
        );

        imgMenuButton = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.menu_button),
                round(XScale * 500),
                round(YScale * 300),
                true
        );

        imgMenuButtonPressed = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.menu_button_inv),
                round(XScale * 500),
                round(YScale * 300),
                true
        );
    }
}
