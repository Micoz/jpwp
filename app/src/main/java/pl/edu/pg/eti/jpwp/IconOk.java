package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class IconOk {
    private int x, y;
    private int alpha;
    private boolean visible;
    private Paint paint;

    public IconOk(int x, int y){
        this.x = x;
        this.y = y;
        this.alpha = 255;
        this.visible = true;
        paint = new Paint();
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

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (alpha>0) {
            alpha -= 5;
            paint.setAlpha(alpha);
        }
    }

    public void draw(Canvas canvas) {
        if (visible) {
            canvas.drawBitmap(GlobalStorage.imgIconOk, x, y, paint);
        }
    }

}
