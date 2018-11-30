package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.graphics.Color;

import static java.lang.StrictMath.round;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    volatile boolean touched = false;
    volatile float touched_x, touched_y;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public static long averageFPS;
    private Paint paint = new Paint();

    private Bitmap manekin = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(getResources(),R.drawable.manekin),
            (int)round(0.7*screenWidth), (int)round(0.9*screenHeight), true);
    private Bitmap floor = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(getResources(),R.drawable.floor),
            screenWidth, screenHeight, true);

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
    }

    public void update() {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(floor, 0, 0, null);

        canvas.drawBitmap(
                manekin,
                screenWidth/2 - manekin.getWidth()/2,
                screenHeight - manekin.getHeight(),
                null
        );

        canvas.drawText("FPS:" + Long.toString(averageFPS),20,50, paint);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        touched_x = event.getX();
        touched_y = event.getY();

        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                touched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touched = true;
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                touched = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                touched = false;
                break;
            default:
        }
        return true; //processed
    }
}
