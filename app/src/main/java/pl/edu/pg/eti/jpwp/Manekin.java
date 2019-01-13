package pl.edu.pg.eti.jpwp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import static java.lang.Math.round;
import static java.lang.Math.signum;

public class Manekin {
    private int x, y;
    private Bitmap img;
    private boolean bonded;
    public Head head;
    public Body body;

    public class Head {
        private int x, y;
        private int tilt;
        private Bitmap img;

        public Head() {
            img = GlobalStorage.imgHead;

            x = GlobalStorage.screenWidth / 2 - img.getWidth() / 2;
            y = GlobalStorage.HEAD_MIN_Y;
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

            int width = GlobalStorage.imgHead.getWidth();
            int height = GlobalStorage.imgHead.getHeight();
            float tiltPercent = (float) tilt / (2 * (GlobalStorage.HEAD_MAX_Y - GlobalStorage.HEAD_MIN_Y));
            int tiltDistance = round(0.1f * width * tiltPercent);

            Matrix tiltMatrix = new Matrix();
            float[] src = {0, 0, 0, height, width, height, width, 0};
            float[] dst = {0 - tiltDistance, 0, 0 + tiltDistance, height, width - tiltDistance, height, width + tiltDistance, 0};
            tiltMatrix.setPolyToPoly(src, 0, dst, 0, 4);
            x = GlobalStorage.screenWidth / 2 - width / 2 - tiltDistance;

            img = Bitmap.createBitmap(GlobalStorage.imgHead, 0, 0, width, height, tiltMatrix, true);
        }

        public int getTilt() {
            return tilt/2;
        }

    }

    /*
     * End of inner class Head
     */

    public class Body {
        private int x, y;
        private Bitmap img;

        public Body() {
            img = GlobalStorage.imgBody;

            x = GlobalStorage.screenWidth/2 - img.getWidth()/2;
            y = GlobalStorage.screenHeight - img.getHeight();
        }
    }

    /*
     * End of inner class Body
     */

    public Manekin(){
        img = GlobalStorage.imgManekin;
        x = GlobalStorage.screenWidth/2 - img.getWidth()/2;
        y = GlobalStorage.screenHeight - img.getHeight();
        bonded = true;
        body = new Body();
        head = new Head();
    }

    public void setBind(boolean bond) {
        bonded = bond;
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        if (bonded) {
            canvas.drawBitmap(img, x, y, null);
        } else {
            canvas.drawBitmap(body.img, body.x, body.y, null);
            canvas.drawBitmap(head.img, head.x, head.y, null);
        }
    }
}
