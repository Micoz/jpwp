package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Wyświetla tekst, który zanika po danym czasie.
 */

public class FadeText {
    private int x, y;
    private int alpha;
    private int color;
    private int timer;
    private int timeMs;
    private boolean visible;
    private String text;

    public FadeText(String text, int x, int y, int color, int timeMs){
        this.x = x;
        this.y = y;
        this.text = text;
        this.color = color;
        this.timeMs = timeMs;
        timer = 0;
        alpha = 255;
        visible = true;
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
            if (timer*33 > timeMs) {
                if (alpha > 0) {
                    alpha -= 40;
                    y -= 5;
                }
                if (alpha <= 0) {
                    alpha = 0;
                    visible = false;
                }
                timer = timeMs+1;
            }
        }
    }

    public void draw(Canvas canvas) {
        if (visible) {
            Paint paint = new Paint();
            paint.setColor(color);
            paint.setAlpha(alpha);
            paint.setTextSize(GlobalStorage.TEXT_SIZE);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas.drawText(text, x, y, paint);
        }
    }

}
