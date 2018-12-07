package pl.edu.pg.eti.jpwp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Ring {
    private int x, y;
    private int radius;
    private Bitmap img;

    public Ring(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.img = DrawableStorage.imgRing;
    }

    public void update() {

    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(img, x, y, null);
    }

}
