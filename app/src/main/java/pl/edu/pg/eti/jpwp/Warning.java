package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

/**
 * Realizuje powiadomienie o zbyt długim czasie oczekiwania na akcję.
 */

public class Warning {
    private int x, y;
    private int xRand, yRand;
    private int timer;
    private int alpha;
    private int alphaSign;
    private Random random;
    private boolean visible;

    public Warning(int x, int y){
        this.x = x;
        this.y = y;
        random = new Random();
        xRand = random.nextInt(11);
        yRand = random.nextInt(11);
        alpha = 255;
        alphaSign = -1;
        visible = true;
    }

    public void setVisible(boolean v) {
        visible = v;
        if (visible) {
            timer = 0;
        }
    }

    public boolean isVisible() {
        return visible;
    }
    public boolean isOver() {
        return (timer > 150);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update() {
        if (visible) {
            timer += 1;
            alpha += alphaSign * 20;
            if (alpha < 0 || alpha > 255) {
                alphaSign = -1 * alphaSign;
                alpha += alphaSign * 20;
            }
            xRand = random.nextInt(11);
            yRand = random.nextInt(11);
        }
    }

    public void draw(Canvas canvas) {
        if (visible) {
            Paint paint = new Paint();
            canvas.drawBitmap(GlobalStorage.imgClock, x + GlobalStorage.imgWarning.getWidth() + (5 - xRand), y + (5 - yRand), null);
            paint.setAlpha(alpha);
            canvas.drawBitmap(GlobalStorage.imgWarning, x, y, paint);
        }
    }

}
