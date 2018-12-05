package pl.edu.pg.eti.jpwp;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;

import static java.lang.StrictMath.round;

public class Manekin {
    public Head head;
    private Bitmap imgHead, imgBody, imgHeadSkew;
    private int body_x, body_y, head_x, head_y;
    private int headTiltX=0, headTiltY=0, headTiltMax, headTiltMin; // 0,0738636363636364 * bodyHeight
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    private class Head {
        private int x, y;
        private Bitmap image, imageSkew;

        public Head() {

        }

        public int getX() {
            return x;
        }

    }

    private class Body {
        private int x, y;
    }

    public Manekin(Context context){
        Bitmap bmp;
        int w, h;
        double scale;

        bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.body);
        scale = (0.75*screenWidth) / bmp.getWidth();
        w = (int)round(scale * bmp.getWidth());
        h = (int)round(scale * bmp.getHeight());
        imgBody = Bitmap.createScaledBitmap(bmp, w, h, true);

        head.x = 2;

        bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.head);
        w = (int) round( scale * bmp.getWidth() );
        h = (int) round( scale * bmp.getHeight() );
        imgHead = Bitmap.createScaledBitmap(bmp, w, h, true);

        body_x = screenWidth/2 - imgBody.getWidth()/2;
        body_y = screenHeight - imgBody.getHeight();
        head_x = screenWidth/2 - imgHead.getWidth()/2;
        head_y = headTiltMax = screenHeight - (int)round(0.65*imgHead.getHeight()) - imgBody.getHeight();
        headTiltMin = screenHeight - (int)round(0.83*imgHead.getHeight()) - imgBody.getHeight();

        setHeadTilt(0, 1000);
    }

    public void update(){

    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(imgBody, body_x, body_y, null);
        if (head_y != headTiltMin) {
            canvas.drawBitmap(imgHeadSkew, head_x, head_y, null);
        } else {
            canvas.drawBitmap(imgHead, head_x, head_y, null);
        }
    }

    public int getHeadX(){
        return head_x;
    }

    public int getHeadY(){
        return head_y;
    }

    public int getHeadWidth(){
        return imgHead.getWidth();
    }

    public int getHeadHeight(){
        return imgHead.getHeight();
    }

    public void setHeadTilt(int dx, int dy){
        headTiltX += dx;
        headTiltY += dy;

        head_y = headTiltMin + (int)round(0.1*headTiltY);
        if (head_y > headTiltMax) {
            head_y = headTiltMax;
            headTiltY = 10*(headTiltMax - headTiltMin);
        }
        if (head_y < headTiltMin) {
            head_y = headTiltMin;
            headTiltY = 0;
        }

        int width = imgHead.getWidth();
        int height = imgHead.getHeight();
        float tiltPercent = (float)headTiltY/(10*(headTiltMax - headTiltMin));
        int tiltDistance = (int)round(0.1*width*tiltPercent);

        Matrix tiltMatrix = new Matrix();
        float[] src = {0, 0, 0, height, width, height, width, 0};
        float [] dst = {0 - tiltDistance, 0, 0 + tiltDistance, height, width - tiltDistance, height, width + tiltDistance, 0};
        tiltMatrix.setPolyToPoly(src, 0, dst, 0, 4);
        head_x = screenWidth/2 - imgHead.getWidth()/2 - tiltDistance;

        imgHeadSkew = Bitmap.createBitmap(imgHead, 0, 0, width, height, tiltMatrix, true);

    }
}
