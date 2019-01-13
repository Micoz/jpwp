package pl.edu.pg.eti.jpwp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import static java.lang.Math.round;

public class Manekin {
    public Head head;
    public Body body;
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public class Head {
        private int x, y;
        private int tilt;
        private Bitmap img, imgTilted;

        public Head() {
            img = GlobalStorage.imgHead;
            imgTilted = img;

            x = screenWidth / 2 - img.getWidth() / 2;
            y = GlobalStorage.HEAD_MIN_Y;

            setTilt(9999);
        }

        public void setTilt(int dt) {
            tilt += dt;

            y = GlobalStorage.HEAD_MIN_Y + tilt/2;
            if (y > GlobalStorage.HEAD_MAX_Y) {
                y = GlobalStorage.HEAD_MAX_Y;
                tilt = 2 * (GlobalStorage.HEAD_MAX_Y - GlobalStorage.HEAD_MIN_Y);
            }
            if (y < GlobalStorage.HEAD_MIN_Y) {
                y = GlobalStorage.HEAD_MIN_Y;
                tilt = 0;
            }

            int width = img.getWidth();
            int height = img.getHeight();
            float tiltPercent = (float) tilt / (2 * (GlobalStorage.HEAD_MAX_Y - GlobalStorage.HEAD_MIN_Y));
            int tiltDistance = (int) round(0.1 * width * tiltPercent);

            Matrix tiltMatrix = new Matrix();
            float[] src = {0, 0, 0, height, width, height, width, 0};
            float[] dst = {0 - tiltDistance, 0, 0 + tiltDistance, height, width - tiltDistance, height, width + tiltDistance, 0};
            tiltMatrix.setPolyToPoly(src, 0, dst, 0, 4);
            x = screenWidth / 2 - img.getWidth() / 2 - tiltDistance;

            imgTilted = Bitmap.createBitmap(img, 0, 0, width, height, tiltMatrix, true);
        }

        public int getTilt() {
            return tilt/2;
        }

        public boolean isLifted() { return (tilt==0);}

    }

    /*
     * End of inner class Head
     */

    public class Body {
        private int x, y;
        private Bitmap img;

        public Body() {
            img = GlobalStorage.imgBody;

            x = screenWidth/2 - img.getWidth()/2;
            y = screenHeight - img.getHeight();
        }
    }

    /*
     * End of inner class Body
     */

    public Manekin(){
        body = new Body();
        head = new Head();
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(body.img, body.x, body.y, null);
        if (head.y != GlobalStorage.HEAD_MIN_Y) {
            canvas.drawBitmap(head.imgTilted, head.x, head.y, null);
        } else {
            canvas.drawBitmap(head.img, head.x, head.y, null);
        }
    }
}
