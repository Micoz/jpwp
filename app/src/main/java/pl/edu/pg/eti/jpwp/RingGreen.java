package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RingGreen {
    private int x, y;
    private int radius;
    private int size;
    private int pulse;
    private boolean visible;

    public RingGreen(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        size = 0;
        pulse = 1;
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
            if ((size > 8) || (size < -8)) {
                pulse = -pulse;
            }
            size += pulse;
        }
    }

    public void draw(Canvas canvas) {
        if (visible) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.GREEN);

            for (int i = 0; i < 20; i++) {
                paint.setAlpha(200 - (i * 10));
                canvas.drawCircle(x, y, radius + size + i, paint);
            }
        }
    }

}
