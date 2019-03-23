package pl.edu.pg.eti.jpwp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Menu główne gry.
 */

public class MainMenu {
    private boolean open = false;
    private long timer = 0;
    private int oldStage = 9;
    private boolean closed = false;
    private boolean restart = false;
    private String[] text = new String[3];
    private Rect[] button = new Rect[4];
    private boolean[] highlight = {false, false, false, false};

    public MainMenu() {
        button[0] = new Rect();
        button[0].set(20, 20, 20 + GlobalStorage.imgMenuIcon.getWidth(), 20 + GlobalStorage.imgMenuIcon.getHeight());

        for (int i=0; i<3; i++) {
            button[i+1] = new Rect();
            button[i+1].set(
                    (GlobalStorage.screenWidth / 2) - GlobalStorage.imgMenuButton.getWidth() / 2,
                    (GlobalStorage.screenHeight / 4) + i*(GlobalStorage.screenHeight / 5),
                    (GlobalStorage.screenWidth / 2) + GlobalStorage.imgMenuButton.getWidth() / 2,
                    (GlobalStorage.screenHeight / 4) + i*(GlobalStorage.screenHeight / 5) + GlobalStorage.imgMenuButton.getHeight()
            );
        }

        text[0] = "POWROT";
        text[1] = "RESTART";
        text[2] = "ZAKONCZ";
    }

    public boolean touched(MotionEvent event) {
        boolean value = false;
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i=0; i<4; i++) {
                    if (button[i].contains(x, y)) {
                        highlight[i] = true;
                        if (i==0 && !open) {
                            value = true;
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (button[0].contains(x, y) && highlight[0]) {
                    value = true;
                }
                for (int i=0; i<4; i++) {
                    if (!button[i].contains(x, y))
                    highlight[i] = false;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (button[0].contains(x, y) && highlight[0]) {
                    timer = System.nanoTime();
                    open = true;
                    value = true;
                }
                if (button[1].contains(x, y) && highlight[1] && open) {
                    timer = System.nanoTime() - timer;
                    open = false;
                    closed = true;
                    value = true;
                }
                if (button[2].contains(x, y) && highlight[2] && open) {
                    open = false;
                    restart = true;
                    value = true;
                }
                if (button[3].contains(x, y) && highlight[3] && open) {
                    open = false;
                    System.exit(0);
                    value = true;
                }

                for (int i=0; i<4; i++) {
                    highlight[i] = false;
                }
                break;
        }

        return value;
    }

    public int getStage(int current_stage) {
        int value = current_stage;
        if (closed) {
            value = oldStage;
            closed = false;
            open = false;
        }
        if (open) {
            value = 9;
            oldStage = current_stage;
        }
        return value;
    }

    public boolean ifRestart() {
        boolean value = restart;
        restart = false;
        return value;
    }

    public boolean isOpen() {
        return open;
    }

    public void draw(Canvas canvas) {
        if (open) {
            Paint paint;
            paint = new Paint();
            paint.setColor(0x50000000);
            paint.setTextSize(GlobalStorage.TEXT_SIZE);
            paint.setTextAlign(Paint.Align.CENTER);

            canvas.drawRect(0, 0, GlobalStorage.screenWidth, GlobalStorage.screenHeight, paint);

            for (int i=0; i<3; i++) {
                if (highlight[i+1]) {
                    paint.setColor(0xEECCCCCC);
                    canvas.drawBitmap(GlobalStorage.imgMenuButtonPressed, button[i+1].left, button[i+1].top, null);
                    canvas.drawText(text[i], button[i+1].left + (button[i+1].width()/2), button[i+1].top + (6*button[i+1].height() / 10), paint);
                } else {
                    paint.setColor(0xEEEEEEEE);
                    canvas.drawBitmap(GlobalStorage.imgMenuButton, button[i+1].left, button[i+1].top, null);
                    canvas.drawText(text[i], button[i+1].left + (button[i+1].width()/2), button[i+1].top + (6*button[i+1].height() / 10), paint);
                }
            }
        } else {
            canvas.drawBitmap(GlobalStorage.imgMenuIcon, button[0].left, button[0].top, null);
        }

    }
}
