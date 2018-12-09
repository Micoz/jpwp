package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ring {
    private int x, y;
    private int radius;
    private int size;
    private int pulse;
    private boolean visible;
    private Paint paint;

    public Ring(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        size = 0;
        pulse = 2;
        this.visible = true;
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
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
        if ((size > 20)||(size < -20)) {
            pulse = -pulse;
        }
        size += pulse;
    }

    public void draw(Canvas canvas) {
        if (visible) {
            for (int i = 0; i < 30; i++) {
                paint.setAlpha(210 - (i * 7));
                canvas.drawCircle(x, y, radius + size + i, paint);
            }
        }
    }

}
