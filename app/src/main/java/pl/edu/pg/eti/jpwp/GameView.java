package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Klasa obsługująca wyświetlanie obiektów, ich aktualizację oraz dotyk.
 */

class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private int oldX[] = {0, 0};
    private int oldY[] = {0, 0};

    private MainThread thread;
    private static Manekin manekin;
    private Hand[] hand = new Hand[2];
    private RingGreen[] ringGreen = new RingGreen[2];
    private RingRed ringRed;
    private IconOk iconOk;
    private Warning warning;
    private FadeText fadeText;
    private MainMenu menu;
    private TempoBar tempoBar;
    private ToneGenerator tone;
    private int nextStage;
    private int counter;
    private int timer;
    private int error;
    private int idle;

    public static long averageFPS;
    public static int GAME_STAGE;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        new GlobalStorage(context);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        tone = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        changeGameStage(0);
        nextStage = 9;
    }

    public void changeGameStage(int stage) {
        GAME_STAGE = stage;

        switch (GAME_STAGE) {
            case 0:
                menu = new MainMenu();
                manekin = new Manekin();
                manekin.setBind(false);
                manekin.head.setTilt(9999);
                counter = 0;
                timer = 3630;
                error = 0;
                idle = 0;
                for (int i = 0; i < 2; i += 1) {
                    hand[i] = new Hand(0,0);
                    hand[i].setVisible(false);
                    ringGreen[i] = new RingGreen(0,0,25);
                    ringGreen[i].setVisible(false);
                }
                ringGreen[0].setPos(GlobalStorage.RING_FOREHEAD_X, GlobalStorage.RING_FOREHEAD_Y + manekin.head.getTilt());
                ringGreen[1].setPos(GlobalStorage.RING_CHIN_X, GlobalStorage.RING_CHIN_Y + manekin.head.getTilt());
                ringRed = new RingRed(GlobalStorage.RING_CHEST_X, GlobalStorage.RING_CHEST_Y, 0);
                ringRed.setVisible(false);
                tempoBar = new TempoBar();
                tempoBar.setVisible(false);
                iconOk = new IconOk(0, 0);
                iconOk.setVisible(false);
                warning = new Warning(0, 0);
                warning.setVisible(false);
                fadeText = new FadeText("",0,0,0,0);
                fadeText.setVisible(false);
                tone.stopTone();
                break;

            case 1:
                for (int i = 0; i < 2; i += 1) {
                    ringGreen[i].setVisible(false);
                    hand[i].setVisible(false);
                }
                manekin.setBind(true);
                tempoBar = new TempoBar();
                tempoBar.setVisible(true);
                ringRed = new RingRed(GlobalStorage.RING_CHEST_X, GlobalStorage.RING_CHEST_Y, 60);
                ringRed.setVisible(true);
                break;

            case 2:
                counter = 2;
                ringRed = new RingRed(GlobalStorage.MOUTH_X, GlobalStorage.MOUTH_Y, 20);
                ringRed.setVisible(true);
                tempoBar.setVisible(false);
                break;

            case 4:
                tempoBar.setVisible(false);
                ringRed.setVisible(false);
                iconOk.setVisible(false);
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
                dist = Math.sqrt(Math.pow(ringGreen[ri].getX() - x[hi], 2) + Math.pow(ringGreen[ri].getY() + manekin.head.getTilt() - (y[hi] + hand[hi].getHeight()/2 +(ri * -1)*hand[hi].getHeight()), 2));

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
            ringGreen[ri].setVisible(show);
        }
        return (stuck[0] && stuck[1]);
    }

    public void update() {
        if (menu.ifRestart()) {
            nextStage = 0;
        }

        if (nextStage != 9) {
            changeGameStage(nextStage);
            nextStage = 9;
        }

        if (!menu.isOpen() && GAME_STAGE != 4) {
            for (int i = 0; i < 2; i += 1) {
                ringGreen[i].update();
            }
            ringGreen[0].setPos(GlobalStorage.RING_FOREHEAD_X, GlobalStorage.RING_FOREHEAD_Y + manekin.head.getTilt());
            ringGreen[1].setPos(GlobalStorage.RING_CHIN_X, GlobalStorage.RING_CHIN_Y + manekin.head.getTilt());
            iconOk.update();
            ringRed.update();
            tempoBar.update();
            fadeText.update();
            manekin.update();
            warning.update();

            if (GAME_STAGE == 0) {
                if (manekin.head.getTilt() == 0) {
                    if (!iconOk.isVisible()) {
                        iconOk = new IconOk(GlobalStorage.screenWidth / 8, GlobalStorage.screenHeight / 4);
                    }
                    if (iconOk.isReady()) {
                        nextStage = 1;
                    }
                } else {
                    iconOk.setVisible(false);
                }
            }

            if (GAME_STAGE == 2) {
                if (iconOk.isReady()) {
                    counter -= 1;
                    if (counter == 0) {
                        nextStage = 1;
                    }
                }
            }

            timer -= 1;
            if (timer == 0) {
                nextStage = 4;
            }

            idle += 1;
            if (idle == 90) {
                warning = new Warning(20, GlobalStorage.screenHeight / 4);
                tempoBar.restart();
                tone.startTone(ToneGenerator.TONE_DTMF_D);
            }
            if (idle < 90 && warning.isVisible()) {
                tone.stopTone();
                warning.setVisible(false);
            }
            if (warning.isOver()) {
                nextStage = 4;
            }
        }
        if ((GAME_STAGE == 4 && !warning.isOver()) || menu.isOpen()) {
            tone.stopTone();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        canvas.drawBitmap(GlobalStorage.imgBackground, 0, 0, null);
        manekin.draw(canvas);
        for (int i=0; i<2; i+=1) {
            hand[i].draw(canvas);
            ringGreen[i].draw(canvas);
        }
        ringRed.draw(canvas);
        tempoBar.draw(canvas);
        fadeText.draw(canvas);
        if (GAME_STAGE == 2 && counter != 0) {
            Paint paint = new Paint();
            paint.setColor(0xBBCCCCCC);
            canvas.drawCircle(GlobalStorage.screenWidth / 8 + 65, GlobalStorage.screenHeight / 4 + 62, GlobalStorage.imgIconOk.getWidth() / 2, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
            paint.setTextSize(GlobalStorage.TEXT_SIZE);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(0xFF1b327c);
            canvas.drawText(Integer.toString(counter),GlobalStorage.screenWidth / 8 + 45, GlobalStorage.screenHeight / 4 + 85, paint);
        }
        iconOk.draw(canvas);
        warning.draw(canvas);
        if (GAME_STAGE == 4) {
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(0x80FFFFFF);
            canvas.drawRect(0, 0, GlobalStorage.screenWidth, GlobalStorage.screenHeight, paint);

            paint.setTextSize(GlobalStorage.TEXT_SIZE);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(8);
            if (warning.isOver()) {
                paint.setColor(0xFFff6666);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("NASTAPILA", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.7f, paint);
                canvas.drawText("NAGLA", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.8f, paint);
                canvas.drawText("SMIERC", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.9f, paint);
            } else
            if (error <= 4) {
                paint.setColor(0xFF6dff74);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("RESUSCYTACJA", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.7f, paint);
                canvas.drawText("WYKONANA", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.8f, paint);
                canvas.drawText("PRAWIDLOWO", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.9f, paint);
            } else {
                paint.setColor(0xFFff8800);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("RESUSCYTACJA", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.7f, paint);
                canvas.drawText("WYKONANA", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.8f, paint);
                canvas.drawText("Z BLEDAMI", GlobalStorage.screenWidth/2, GlobalStorage.screenHeight * 0.9f, paint);
            }
        }
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setTextSize(GlobalStorage.TEXT_SIZE);
        paint.setTextAlign(Paint.Align.RIGHT);
        paint.setColor(0xFFFFFFFF);
        canvas.drawText(Integer.toString(timer/30), GlobalStorage.screenWidth - 20, GlobalStorage.TEXT_SIZE, paint);

        menu.draw(canvas);
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
        if (!menu.touched(event)) {

            switch (GAME_STAGE) {
                case 0:
                    switch (event.getAction()) {

                        case MotionEvent.ACTION_DOWN:
                            idle = 0;
                            hand[0].setPos((int) event.getX(0), (int) event.getY(0));
                            hand[0].setVisible(true);
                            for (int i = 0; i < 2; i += 1) {
                                ringGreen[i].setVisible(true);
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            idle = 0;
                            for (int i = 0; i < 2; i += 1) {
                                hand[i].setVisible(false);
                                ringGreen[i].setVisible(false);
                            }
                            oldX[0] = oldX[1] = 0;
                            oldY[0] = oldY[1] = 0;
                            manekin.head.setTilt(9999);
                            break;

                        case MotionEvent.ACTION_MOVE:
                            idle = 0;
                            int[] newX = {(int) event.getX(), 0};
                            int[] newY = {(int) event.getY(), 0};

                            if (event.getPointerCount() >= 2) {
                                newX[1] = (int) event.getX(1);
                                newY[1] = (int) event.getY(1);

                                for (int i = 0; i < 2; i += 1) {
                                    if (!hand[i].isVisible()) {
                                        hand[i].setVisible(true);
                                    }
                                    hand[i].setPos(newX[i], newY[i]);
                                }
                            } else {
                                if (!hand[0].isVisible()) {
                                    hand[0].setVisible(true);
                                }
                                hand[0].setPos(newX[0], newY[0]);
                                if (hand[1].isVisible()) {
                                    hand[1].setVisible(false);
                                }
                            }

                            for (int i = 0; i < 2; i += 1) {
                                if (oldX[i] == 0) oldX[i] = newX[i];
                                if (oldY[i] == 0) oldY[i] = newY[i];
                            }

                            if (holdingHead(newX, newY)) {
                                manekin.head.setTilt(((newY[0] - oldY[0]) + (newY[1] - oldY[1])) / 2);

                                if (hand[0].getY() < hand[1].getY()) {
                                    hand[0].setPos(GlobalStorage.HAND_FOREHEAD_X, GlobalStorage.HAND_FOREHEAD_Y + manekin.head.getTilt());
                                    hand[1].setPos(GlobalStorage.HAND_CHIN_X, GlobalStorage.HAND_CHIN_Y + manekin.head.getTilt());
                                } else {
                                    hand[0].setPos(GlobalStorage.HAND_CHIN_X, GlobalStorage.HAND_CHIN_Y + manekin.head.getTilt());
                                    hand[1].setPos(GlobalStorage.HAND_FOREHEAD_X, GlobalStorage.HAND_FOREHEAD_Y + manekin.head.getTilt());
                                }
                            } else {
                                manekin.head.setTilt(9999);
                            }

                            oldX = newX;
                            oldY = newY;
                            break;

                        case MotionEvent.ACTION_CANCEL:
                            for (int i = 0; i < 2; i += 1) {
                                hand[i].setVisible(false);
                                ringGreen[i].setVisible(false);
                            }
                            break;
                    }
                    break;

                case 1:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (ringRed.contains((int)event.getX(), (int)event.getY())) {
                                idle = 0;
                                tone.startTone(ToneGenerator.TONE_DTMF_D, 200);
                                ringRed.press();
                                tempoBar.measure();
                                switch (tempoBar.getStatus()) {
                                    case 0:
                                        fadeText = new FadeText("ZA WOLNO", GlobalStorage.screenWidth / 2, GlobalStorage.screenHeight / 12, 0xDDff6666, 200);
                                        error += 1;
                                        break;

                                    case 1:
                                        fadeText = new FadeText("OK", GlobalStorage.screenWidth / 2, GlobalStorage.screenHeight / 12, 0xDD6dff74, 200);
                                        break;

                                    case 2:
                                        fadeText = new FadeText("ZA SZYBKO", GlobalStorage.screenWidth / 2, GlobalStorage.screenHeight / 12, 0xDDff6666, 200);
                                        error += 1;
                                        break;

                                    case 9:
                                        nextStage = 2;
                                        break;
                                }
                            }
                    }
                    break;

                case 2:
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (ringRed.contains((int)event.getX(), (int)event.getY())) {
                                idle = 0;
                                iconOk = new IconOk(GlobalStorage.screenWidth / 8, GlobalStorage.screenHeight / 4);
                            }
                            break;

                        case MotionEvent.ACTION_MOVE:
                            if (!ringRed.contains((int)event.getX(), (int)event.getY())) {
                                iconOk.setVisible(false);
                            } else {
                                idle = 0;
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            iconOk.setVisible(false);
                            break;
                    }
                    break;
            }
        } else {
            int temp = GAME_STAGE;
            GAME_STAGE = menu.getStage(GAME_STAGE);
            if (temp != GAME_STAGE && GAME_STAGE != 9) {
                if (warning.isVisible()) {
                    tone.startTone(ToneGenerator.TONE_DTMF_D);
                }
            }
        }

        return true;
    }
}
