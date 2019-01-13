package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class IconOk {
    private int x, y;
    private float angle;
    private int alpha;
    private boolean ready;
    private boolean visible;

    public IconOk(int x, int y){
        this.x = x;
        this.y = y;
        angle = 0;
        alpha = 255;
        visible = true;
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
        if (visible) {
            if (angle > 360) {
                angle = 360;
                ready = true;
            } else
            if (angle < 360) {
                angle += 11.88;
            } else {
                alpha -= 35;
                y -= 5;
                if (alpha < 0) {
                    alpha = 0;
                    visible = false;
                }
            }
        }
    }

    public boolean isReady() {
        boolean value = ready;
        ready = false;
        return value;
    }

    public void draw(Canvas canvas) {
        if (visible) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(12);
            paint.setColor(0xFF084c06);
            paint.setAlpha(alpha);

            RectF arc = new RectF(x, y, x + GlobalStorage.imgIconOk.getWidth(), y + GlobalStorage.imgIconOk.getHeight());

            canvas.drawArc(arc, 0, angle, false, paint);
            if (angle == 360) {
                canvas.drawBitmap(GlobalStorage.imgIconOk, x, y, paint);
            }
        }
    }

}
