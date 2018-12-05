package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.graphics.Color;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    private Paint paint = new Paint();
    private Bitmap imgBackground;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public static Manekin manekin;
    public static long averageFPS;
    public static int GAME_STAGE = 0;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        manekin = new Manekin(context);
        imgBackground = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(),R.drawable.floor),
                screenWidth, screenHeight, true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        setFocusable(true);
        setOnTouchListener(new TouchListener());
    }

    public void update() {
        manekin.update();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(imgBackground, 0, 0, null);
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
