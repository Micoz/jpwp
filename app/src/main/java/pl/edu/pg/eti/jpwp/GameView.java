package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.graphics.Color;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Paint paint = new Paint();
    private static Manekin manekin;

    public static long averageFPS;
    public static int GAME_STAGE = 0;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        new DrawableStorage(context);
        thread = new MainThread(getHolder(), this);
        manekin = new Manekin(context);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        setFocusable(true);
        setOnTouchListener(new TouchListener());
    }

    public void update() {
        manekin.update();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(DrawableStorage.imgBackground, 0, 0, null);
        manekin.draw(canvas);
        canvas.drawText("FPS:" + Long.toString(averageFPS),20,50, paint);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }
}
