package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.graphics.Paint;
import android.graphics.Color;

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private int x_old[] = {0, 0};
    private int y_old[] = {0, 0};

    private MainThread thread;
    private Paint paint = new Paint();
    private static Manekin manekin;
    private Hand[] hand;
    private Ring[] ring;
    private IconOk icon;

    public static long averageFPS;
    public static int GAME_STAGE;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        new GlobalStorage(context);
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
        GAME_STAGE = stage;

        switch (GAME_STAGE) {
            case 0:
                manekin = new Manekin();
                for (int i = 0; i < 2; i += 1) {
                    hand[i] = new Hand(0,0);
                    hand[i].setVisible(false);
                    ring[i] = new Ring(0,0,25);
                    ring[i].setVisible(true);
                }
                ring[0].setPos(GlobalStorage.RING_FOREHEAD_X, GlobalStorage.RING_FOREHEAD_Y + manekin.head.getTilt());
                ring[1].setPos(GlobalStorage.RING_CHIN_X, GlobalStorage.RING_CHIN_Y + manekin.head.getTilt());
                break;

            case 1:
                for (int i = 0; i < 2; i += 1) {
                    ring[i] = new Ring(0,0,25);
                    ring[i].setVisible(false);
                    hand[i].setVisible(false);
                }
                icon = new IconOk(400, 400);
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

                if (dist < GlobalStorage.HAND_DISTANCE) {
                    if (ri==0) {
                        hand[hi].setPos(GlobalStorage.HAND_FOREHEAD_X, GlobalStorage.HAND_FOREHEAD_Y + manekin.head.getTilt());
                    } else {
                        hand[hi].setPos(GlobalStorage.HAND_CHIN_X, GlobalStorage.HAND_CHIN_Y + manekin.head.getTilt());
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
                if (manekin.head.isLifted()) {
                    changeGameStage(1);
                }
                break;

            case 1:
                icon.update();
                break;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(GlobalStorage.imgBackground, 0, 0, null);
        manekin.draw(canvas);
        canvas.drawText("FPS:" + Long.toString(averageFPS),20,50, paint);

        for (int i=0; i<2; i+=1) {
            hand[i].draw(canvas);
            ring[i].draw(canvas);
        }
        icon.draw(canvas);
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
        switch (GAME_STAGE) {
            case 0: {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        hand[0].setPos((int) event.getX(0), (int) event.getY(0));
                        hand[0].setVisible(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        for (int i = 0; i < 2; i += 1) {
                            hand[i].setVisible(false);
                            ring[i].setVisible(true);
                        }
                        x_old[0] = x_old[1] = 0;
                        y_old[0] = y_old[1] = 0;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        int[] x = {(int) event.getX(), 0};
                        int[] y = {(int) event.getY(), 0};

                        if (event.getPointerCount() >= 2) {
                            x[1] = (int) event.getX(1);
                            y[1] = (int) event.getY(1);

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

                        for (int i = 0; i < 2; i += 1) {
                            if (x_old[i] == 0) x_old[i] = x[i];
                            if (y_old[i] == 0) y_old[i] = y[i];
                        }

                        if (holdingHead(x, y)) {
                            manekin.head.setTilt(((y[0] - y_old[0]) + (y[1] - y_old[1])) / 2);

                            ring[0].setPos(GlobalStorage.RING_FOREHEAD_X, GlobalStorage.RING_FOREHEAD_Y + manekin.head.getTilt());
                            ring[1].setPos(GlobalStorage.RING_CHIN_X, GlobalStorage.RING_CHIN_Y + manekin.head.getTilt());

                            if (hand[0].getY() < hand[1].getY()) {
                                hand[0].setPos(GlobalStorage.HAND_FOREHEAD_X, GlobalStorage.HAND_FOREHEAD_Y + manekin.head.getTilt());
                                hand[1].setPos(GlobalStorage.HAND_CHIN_X, GlobalStorage.HAND_CHIN_Y + manekin.head.getTilt());
                            } else {
                                hand[0].setPos(GlobalStorage.HAND_CHIN_X, GlobalStorage.HAND_CHIN_Y + manekin.head.getTilt());
                                hand[1].setPos(GlobalStorage.HAND_FOREHEAD_X, GlobalStorage.HAND_FOREHEAD_Y + manekin.head.getTilt());
                            }
                        }
                        x_old = x;
                        y_old = y;
                        break;

                    case MotionEvent.ACTION_CANCEL:
                        for (int i = 0; i < 2; i += 1) {
                            hand[i].setVisible(false);
                            ring[i].setVisible(true);
                        }
                        break;
                }
                break;
            }

            case 1: {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        icon = new IconOk((int) event.getX(), (int) event.getY());
                        break;
                }
            }
        }

        return true;
    }
}
