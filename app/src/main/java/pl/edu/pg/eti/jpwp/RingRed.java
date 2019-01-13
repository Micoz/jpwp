package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RingRed {
    private int x, y;
    private int radius;
    private int size;
    private int pulse;
    private int speed;
    private boolean visible;
    private boolean pulsing;

    public RingRed(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        size = 0;
        pulse = 0;
        speed = 0;
        visible = true;
        pulsing = false;
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

    public void press() {
        pulsing = true;
        pulse = 20;
        speed = 10;
    }

    public boolean contains(int a, int b) {
        return ((a > x-2*radius) && (a < x+2*radius) && (b > y-2*radius) && (b < y+2*radius));
    }

    public void update() {
        if (visible) {
            size += 1;
            if (size > 12) {
                size = 0;
            }

            if (pulsing) {
                speed -= 2;
                pulse += speed;
                if (pulse < 0) {
                    speed = 0;
                    pulse = 0;
                    pulsing = false;
                }
            }
        }
    }

    public void draw(Canvas canvas) {
        if (visible) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setColor(0xFFFF4040);

            for (int i = 0; i < 5; i++) {
                paint.setAlpha(200 - (i * 40) - (size * 3));
                canvas.drawCircle(x, y, radius + (i*12 + size) + pulse*2, paint);
            }
            paint.setAlpha(200);
            canvas.drawCircle(x, y, radius + pulse, paint);
        }
    }

}
