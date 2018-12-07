package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DrawableStorage {
    public static Bitmap imgBackground;
    public static Bitmap imgRing;

    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public DrawableStorage(Context context){
        imgBackground = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.floor),
                screenWidth, screenHeight, true);

        imgRing = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.getResources(),R.drawable.ring), 40, 40,true);


    }
}
