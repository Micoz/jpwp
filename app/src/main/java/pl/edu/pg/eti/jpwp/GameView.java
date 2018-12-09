package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.graphics.Color;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private final int HAND_FOREHEAD_X = 388;
    private final int HAND_FOREHEAD_Y = 169;
    private final int HAND_CHIN_X = 395;
    private final  int HAND_CHIN_Y = 747;

    private final int RING_FOREHEAD_X = 360;
    private final int RING_FOREHEAD_Y = 300;
    private final int RING_CHIN_X = 360;
    private final int RING_CHIN_Y = 600;

    private final int HAND_DISTANCE = 120;

    private int x_old[] = {0, 0};
    private int y_old[] = {0, 0};

    private MainThread thread;
    private Paint paint = new Paint();
    private static Manekin manekin;
    private Hand[] hand;
    private Ring[] ring;

    public static long averageFPS;
    public static int GAME_STAGE;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        new DrawableStorage(context);
        thread = new MainThread(getHolder(), this);
        hand = new Hand[2];
        ring = new Ring[2];
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        paint.setTextSize(40);
        setFocusable(true);
        changeGameStage(0);
    }

    public void changeGameStage(int stage) {
        switch (stage) {
            case 0:
                manekin = new Manekin();
                for (int i = 0; i < 2; i += 1) {
                    hand[i] = new Hand(0,0);
                    hand[i].setVisible(false);
                    ring[i] = new Ring(0,0,30);
                    ring[i].setVisible(true);
                }
                ring[0].setPos(RING_FOREHEAD_X, RING_FOREHEAD_Y);
                ring[1].setPos(RING_CHIN_X, RING_CHIN_Y);
                GAME_STAGE = stage;
                break;
        }
    }

    public boolean holdingHead(int[] x, int[] y) {
        double dist;
        boolean show;
        boolean[] stuck = {false, false};

        for (int ri = 0; ri < 2; ri += 1) {
            show = true;
            for (int hi = 0; hi < 2; hi += 1) {
                dist = Math.sqrt(Math.pow(ring[ri].getX() - x[hi], 2) + Math.pow(ring[ri].getY() + manekin.head.getTilt() - (y[hi] + hand[hi].getHeight()/2 +(ri * -1)*hand[hi].getHeight()), 2));

                if (dist < HAND_DISTANCE) {
                    if (ri==0) {
                        hand[hi].setPos(HAND_FOREHEAD_X, HAND_FOREHEAD_Y + 2*manekin.head.getTilt());
                    } else {
                        hand[hi].setPos(HAND_CHIN_X, HAND_CHIN_Y + manekin.head.getTilt());
                    }
                    if (hand[hi].isVisible()) show = false;
                    stuck[hi]=true;
                }
            }
            ring[ri].setVisible(show);
        }
        return (stuck[0] && stuck[1]);
    }

    public void update() {
        switch (GAME_STAGE) {
            case 0:
                for (int i = 0; i < 2; i += 1) {
                    ring[i].update();
                }
                manekin.update();
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        //canvas.drawBitmap(DrawableStorage.imgBackground, 0, 0, null);
        manekin.draw(canvas);
        canvas.drawText("FPS:" + Long.toString(averageFPS),20,50, paint);

        for (int i=0; i<2; i+=1) {
            hand[i].draw(canvas);
            ring[i].draw(canvas);
        }
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
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (GAME_STAGE) {
            case 0: {
                switch(event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        hand[0].setPos((int) event.getX(0), (int) event.getY(0));
                        hand[0].setVisible(true);
                        return true;

                    case MotionEvent.ACTION_UP:
                        for (int i=0; i<2; i+=1) {
                            hand[i].setVisible(false);
                            ring[i].setVisible(true);
                        }
                        x_old[0] = x_old[1] = 0;
                        y_old[0] = y_old[1] = 0;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int[] x = {(int)event.getX(), 0};
                        int[] y = {(int)event.getY(), 0};

                        if (event.getPointerCount() >= 2) {
                            x[1] = (int)event.getX(1);
                            y[1] = (int)event.getY(1);

                            for (int i = 0; i < 2; i += 1) {
                                if (!hand[i].isVisible()) {
                                    hand[i].setVisible(true);
                                }
                                hand[i].setPos(x[i], y[i]);
                            }
                        } else {
                            if (!hand[0].isVisible()) {
                                hand[0].setVisible(true);
                            }
                            hand[0].setPos(x[0], y[0]);
                            if (hand[1].isVisible()) {
                                hand[1].setVisible(false);
                            }
                        }

                        for (int i=0; i<2; i+=1) {
                            if (x_old[i] == 0) x_old[i] = x[i];
                            if (y_old[i] == 0) y_old[i] = y[i];
                        }

                        if (holdingHead(x, y)) {
                            manekin.head.setTilt(( (y[0]-y_old[0]) + (y[1]-y_old[1]) )/2);

                            ring[0].setPos(RING_FOREHEAD_X, RING_FOREHEAD_Y + manekin.head.getTilt());
                            ring[1].setPos(RING_CHIN_X, RING_CHIN_Y + manekin.head.getTilt());

                            if (hand[0].getY() < hand[1].getY()) {
                                hand[0].setPos(HAND_FOREHEAD_X, HAND_FOREHEAD_Y + 2*manekin.head.getTilt());
                                hand[1].setPos(HAND_CHIN_X, HAND_CHIN_Y + manekin.head.getTilt());
                            } else {
                                hand[0].setPos(HAND_CHIN_X, HAND_CHIN_Y + manekin.head.getTilt());
                                hand[1].setPos(HAND_FOREHEAD_X, HAND_FOREHEAD_Y + 2*manekin.head.getTilt());
                            }
                        }
                        x_old = x;
                        y_old = y;
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        for (int i=0; i<2; i+=1) {
                            hand[i].setVisible(false);
                            ring[i].setVisible(true);
                        }
                        break;
                }
                break;
            }
            case 1: {
                break;
            }
        }

        return true;
    }
}
