package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import static java.lang.Math.round;
import static java.lang.Math.signum;

public class TempoBar {
    private Rect bar;
    private int x, xPos;
    private int length;
    private int counter;
    private int timer;
    private int status;
    private boolean visible;
    private boolean start;

    public TempoBar(){
        bar = new Rect();
        bar.set(round(0.2f*GlobalStorage.screenWidth), round(0.1f*GlobalStorage.screenHeight), round(0.8f*GlobalStorage.screenWidth), round(0.15f*GlobalStorage.screenHeight));
        length = bar.right - bar.left;
        x = xPos = (bar.left + bar.right) / 2;
        visible = true;
        status = 10;
        start = false;
        counter = 30;
    }

    public void measure() {
        if (start) {
            int percentage;
            percentage = round((timer - 30) / -0.225f);
            if (percentage < 4) {
                percentage = 4;
            }
            if (percentage > 96) {
                percentage = 96;
            }

            status = percentage / 33;
            xPos = bar.left + ((percentage * length) / 100);
        } else {
            start=true;
        }

        timer = 0;
        counter -= 1;
        if (counter == 0) {
            status = 9;
        }
    }

    public int getStatus() {
        return status;
    }

    public void setVisible(boolean v) {
        visible = v;
    }

    public boolean isVisible() {
        return visible;
    }

    public void update() {
        if (visible) {
            timer += 1;

            x += (signum(xPos - x) + (xPos - x) / 10);
        }
    }

    public void draw(Canvas canvas) {
        if (visible) {
            Paint paint = new Paint();

            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0xFFff6666);
            canvas.drawRect(bar.left, bar.top, bar.left + round(length*0.33f), bar.bottom, paint);
            canvas.drawRect(bar.right - round(length*0.33f), bar.top, bar.right, bar.bottom, paint);
            paint.setColor(0xFF6dff74);
            canvas.drawRect(bar.left + round(length*0.33f), bar.top, bar.right - round(length*0.33f), bar.bottom, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            //paint.setColor(0xFF560099);
            paint.setColor(0xFF1b327c);
            canvas.drawRect(bar, paint);

            paint.setColor(0xFFFFFFFF);
            canvas.drawLine(x, bar.top, x, bar.bottom, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(GlobalStorage.TEXT_SIZE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(0xFF1b327c);
            canvas.drawText(Integer.toString(counter), GlobalStorage.RING_CHEST_X, GlobalStorage.RING_CHEST_Y + GlobalStorage.TEXT_SIZE/3, paint);
        }
    }

}
