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

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private MainThread thread;
    Manekin manekin;
    private Paint paint = new Paint();
    private Bitmap imgFloor;
    private boolean touched = false;
    private boolean moveHead = false;
    private float touched_x, touched_y, touched_x_old, touched_y_old;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public static long averageFPS;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        manekin = new Manekin(context);
        imgFloor = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(),R.drawable.floor),
                screenWidth, screenHeight, true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        setFocusable(true);
    }

    public void update() {
        manekin.update();
        manekin.head.getX();

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(imgFloor, 0, 0, null);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touched_x = event.getX();
        touched_y = event.getY();

        manekin.head.getX();

        int action = event.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN:
                touched = true;
                if ((touched_x > manekin.getHeadX()) && (touched_x < manekin.getHeadX() + manekin.getHeadWidth())
                        && (touched_y > manekin.getHeadY()) && (touched_y < manekin.getHeadY() + manekin.getHeadHeight())) {
                    moveHead = true;
                }
                touched_x_old = event.getX();
                touched_y_old = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if ((touched_x < manekin.getHeadX()) || (touched_x > manekin.getHeadX() + manekin.getHeadWidth())
                        || (touched_y < manekin.getHeadY()) || (touched_y > manekin.getHeadY() + manekin.getHeadHeight())) {
                    moveHead = false;
                }
                if (moveHead) {
                    manekin.setHeadTilt(
                            (int) touched_x - (int) touched_x_old,
                            (int) touched_y - (int) touched_y_old
                    );
                }
                touched = true;
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                moveHead = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                touched = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                touched = false;
                break;
            default:
        }

        touched_x_old = touched_x;
        touched_y_old = touched_y;
        return true; //processed
    }
}
