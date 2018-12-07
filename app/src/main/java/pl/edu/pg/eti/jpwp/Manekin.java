package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.MotionEvent;

import static java.lang.StrictMath.round;

public class Manekin {
    public Head head;
    public Body body;
    private Paint paint = new Paint();
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public class Head {
        private int x, y;
        private int tilt;
        private Rect area;
        private final int MAX_Y, MIN_Y;
        private Bitmap img, imgTilted;

        public Head(Bitmap bmp, double scale) {
            img = Bitmap.createScaledBitmap(bmp,
                    (int)round(scale * bmp.getWidth()),
                    (int)round(scale * bmp.getHeight()),
                    true);

            MAX_Y = screenHeight - body.img.getHeight() - (int)round(0.65 * img.getHeight());
            MIN_Y = screenHeight - body.img.getHeight() - (int)round(0.83 * img.getHeight());

            x = screenWidth / 2 - img.getWidth() / 2;
            y = MIN_Y;

            area = new Rect(x, y, x+img.getWidth(),y+img.getHeight());
        }

        public void setTilt(int dt) {
            tilt += dt;

            y = MIN_Y + (int) round(0.1 * tilt);
            if (y > MAX_Y) {
                y = MAX_Y;
                tilt = 10 * (MAX_Y - MIN_Y);
            }
            if (y < MIN_Y) {
                y = MIN_Y;
                tilt = 0;
            }

            int width = img.getWidth();
            int height = img.getHeight();
            float tiltPercent = (float) tilt / (10 * (MAX_Y - MIN_Y));
            int tiltDistance = (int) round(0.1 * width * tiltPercent);

            Matrix tiltMatrix = new Matrix();
            float[] src = {0, 0, 0, height, width, height, width, 0};
            float[] dst = {0 - tiltDistance, 0, 0 + tiltDistance, height, width - tiltDistance, height, width + tiltDistance, 0};
            tiltMatrix.setPolyToPoly(src, 0, dst, 0, 4);
            x = screenWidth / 2 - img.getWidth() / 2 - tiltDistance;

            imgTilted = Bitmap.createBitmap(img, 0, 0, width, height, tiltMatrix, true);

        }
    }

    /*
     * End of inner class Head
     */

    public class Body {
        private int x, y;
        private Rect area;
        private Bitmap img;
        private Ring ring;
        private double chestCircle;
        private int chestCircleSpeed;

        public Body(Bitmap bmp, double scale) {
            img = Bitmap.createScaledBitmap(bmp,
                    (int)round(scale * bmp.getWidth()),
                    (int)round(scale * bmp.getHeight()),
                    true);

            x = screenWidth/2 - img.getWidth()/2;
            y = screenHeight - img.getHeight();

            area = new Rect(
                    x + 3*img.getWidth()/10,
                    y + 4*img.getHeight()/10,
                    x + 7*img.getWidth()/10,
                    y + 6*img.getHeight()/10
            );
            chestCircle = 0;
            chestCircleSpeed = 1;
        }
    }

    /*
     * End of inner class Body
     */

    public Manekin(Context context){
        double scale;
        scale = (0.75*screenWidth) / BitmapFactory.decodeResource(context.getResources(),R.drawable.body).getWidth();
        body = new Body(BitmapFactory.decodeResource(context.getResources(),R.drawable.body), scale);
        head = new Head(BitmapFactory.decodeResource(context.getResources(),R.drawable.head), scale);
        head.setTilt(1000);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        body.ring = new Ring(body.x + body.img.getWidth() / 2, body.y + body.img.getHeight() / 2, 50);
    }

    public void update(){
        MotionEvent e = TouchListener.eventBuffer;

        if (e != null) {

            switch (GameView.GAME_STAGE) {
                case 0: {
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (head.area.contains((int) e.getX(), (int) e.getY())) {
                                head.setTilt((int) e.getY() - (int) e.getHistoricalY(0));
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                        case MotionEvent.ACTION_OUTSIDE:
                            break;
                        default:
                    }
                    break;
                }
                case 1: {
                    switch (e.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            break;
                        case MotionEvent.ACTION_MOVE:
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            break;
                        case MotionEvent.ACTION_OUTSIDE:
                            break;
                        default:
                    }
                    break;
                }
            }
        }

        head.area.set(
                head.x,
                head.y,
                head.x+head.img.getWidth(),
                head.y+head.img.getHeight()
        );

        body.area.set(
                body.x + 3*body.img.getWidth()/10,
                body.y + 4*body.img.getHeight()/10,
                body.x + 7*body.img.getWidth()/10,
                body.y + 6*body.img.getHeight()/10
        );

        if (body.chestCircleSpeed > 0) {
            body.chestCircle += 1.2;
            if (body.chestCircle > 20){
                body.chestCircleSpeed = -1;
            }
        } else {
            body.chestCircle -= 1.2;
            if (body.chestCircle < 0){
                body.chestCircleSpeed = 1;
            }
        }
        body.ring.update();
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(body.img, body.x, body.y, null);
        if (head.y != head.MIN_Y) {
            canvas.drawBitmap(head.imgTilted, head.x, head.y, null);
        } else {
            canvas.drawBitmap(head.img, head.x, head.y, null);
        }

        body.ring.draw(canvas);
        //canvas.drawRect(body.area, paint);
        //paint.setStyle(Paint.Style.STROKE);
        /*for (int i=0; i<50; i++)
        {

            paint.setAlpha(200 - (i*4));
            canvas.drawCircle(
                    body.x + body.img.getWidth() / 2,
                    body.y + body.img.getHeight() / 2,
                    (body.img.getWidth() / 8) + (int)body.chestCircle + i,
                    paint
            );
        }*/
    }
}
