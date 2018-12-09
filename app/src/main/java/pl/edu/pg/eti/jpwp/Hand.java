package pl.edu.pg.eti.jpwp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import static java.lang.Math.round;

public class Hand {
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    private int x, y;
    private int offset;
    private int changeHeight;
    private boolean stuck;
    private boolean visible;
    private Bitmap img;

    public Hand(int x, int y) {
        this.x = 0;
        this.y = 0;
        changeHeight = (int)round(0.3*screenHeight);
        if (y >= changeHeight) {
            img = DrawableStorage.imgHandDown;
        } else {
            img = DrawableStorage.imgHandUp;
        }
        this.visible = true;

        setPos(x, y);
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setStuck(boolean s) {
        stuck = s;
    }

    public boolean isStuck() {
        return stuck;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return img.getHeight();
    }

    public void setPos(int x, int y) {
        if (!stuck) {
            if (y < changeHeight) {
                if (this.y >= changeHeight) img = DrawableStorage.imgHandUp;
                if (changeHeight - y < img.getHeight()) {
                    offset = -(img.getHeight()/2 - (changeHeight - y)/2);
                } else {
                    offset = 0;
                }
            }
            if (y > changeHeight) {
                if (this.y <= changeHeight) img = DrawableStorage.imgHandDown;
                if (y - changeHeight < img.getHeight()) {
                    offset = -(img.getHeight()/2 + (y - changeHeight)/2);
                } else {
                    offset = -img.getHeight();
                }
            }
            this.x = x;
            this.y = y;
        }
    }

    public void draw(Canvas canvas) {
        if (visible) {
            canvas.drawBitmap(img, x - (img.getWidth() / 2), y + offset, null);
        }
    }
}
