package pl.edu.pg.eti.jpwp;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Hand {
    private int x, y;
    private int offset;
    private boolean visible;
    private Bitmap img;

    public Hand(int x, int y) {
        this.x = 0;
        this.y = 0;
        if (y >= GlobalStorage.HAND_FLIP_Y) {
            img = GlobalStorage.imgHandDown;
        } else {
            img = GlobalStorage.imgHandUp;
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
        if (y < GlobalStorage.HAND_FLIP_Y) {
            if (this.y >= GlobalStorage.HAND_FLIP_Y) img = GlobalStorage.imgHandUp;
            if (GlobalStorage.HAND_FLIP_Y - y < img.getHeight()) {
                offset = -(img.getHeight()/2 - (GlobalStorage.HAND_FLIP_Y - y)/2);
            } else {
                offset = 0;
            }
        }
        if (y > GlobalStorage.HAND_FLIP_Y) {
            if (this.y <= GlobalStorage.HAND_FLIP_Y) img = GlobalStorage.imgHandDown;
            if (y - GlobalStorage.HAND_FLIP_Y < img.getHeight()) {
                offset = -(img.getHeight()/2 + (y - GlobalStorage.HAND_FLIP_Y)/2);
            } else {
                offset = -img.getHeight();
            }
        }
        this.x = x;
        this.y = y;
    }

    public void draw(Canvas canvas) {
        if (visible) {
            canvas.drawBitmap(img, x - (img.getWidth() / 2), y + offset, null);
        }
    }
}
