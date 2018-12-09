package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static java.lang.StrictMath.round;

public class DrawableStorage {
    //public static Bitmap imgBackground;
    public static Bitmap imgBody;
    public static Bitmap imgHead;
    public static Bitmap imgRing;
    public static Bitmap imgHandUp;
    public static Bitmap imgHandDown;

    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    //private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public DrawableStorage(Context context){
        double scale = (0.75*screenWidth) / BitmapFactory.decodeResource(context.getResources(),R.drawable.body).getWidth();

        /*imgBackground = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.floor),
                screenWidth, screenHeight, true);*/

        imgBody = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.body),
                (int)round(scale * BitmapFactory.decodeResource(context.getResources(),R.drawable.body).getWidth()),
                (int)round(scale * BitmapFactory.decodeResource(context.getResources(),R.drawable.body).getHeight()),
                true
        );

        imgHead = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.head),
                (int)round(scale * BitmapFactory.decodeResource(context.getResources(),R.drawable.head).getWidth()),
                (int)round(scale * BitmapFactory.decodeResource(context.getResources(),R.drawable.head).getHeight()),
                true);

        imgRing = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.ring),
                40,
                40,
                true
        );

        imgHandDown = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.hand_down),
                240,
                240,
                true
        );

        imgHandUp = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.hand_up),
                240,
                240,
                true
        );


    }
}
