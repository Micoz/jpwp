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
    private Bitmap imgHead, imgBody, imgHeadSkew;
    private int body_x, body_y, head_x, head_y;
    private int headTiltX=0, headTiltY=0, headTiltMax, headTiltMin; // 0,0738636363636364 * bodyHeight
    private int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
    private int screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

    public Manekin(Context context){
        Bitmap bmp;
        int w, h;
        double scale;

        bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.body);
        scale = (0.75*screenWidth) / bmp.getWidth();
        w = (int)round(scale * bmp.getWidth());
        h = (int)round(scale * bmp.getHeight());
        imgBody = Bitmap.createScaledBitmap(bmp, w, h, true);

        bmp = BitmapFactory.decodeResource(context.getResources(),R.drawable.head);
        w = (int) round( scale * bmp.getWidth() );
        h = (int) round( scale * bmp.getHeight() );
        imgHead = Bitmap.createScaledBitmap(bmp, w, h, true);

        imgHeadSkew = imgHead;

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

        int w = imgHead.getWidth();
        int h = imgHead.getHeight();
        float p = (float)headTiltY/(10*(headTiltMax - headTiltMin));
        int d = (int)round(0.1*w*p);
        int l = (int)round(0.05*headTiltX);
        Matrix tiltMatrix = new Matrix();
        float[] src = {0, 0, 0, h, w, h, w, 0};
        float [] dst = {0 - d, 0, 0 + d, h, w - d, h, w + d, 0};
        tiltMatrix.setPolyToPoly(src, 0, dst, 0, 4);
        head_x = screenWidth/2 - imgHead.getWidth()/2 - d;

        System.out.println("d: "+d);
        System.out.println("p: "+p);

        imgHeadSkew = Bitmap.createBitmap(imgHead, 0, 0, w, h, tiltMatrix, true);

    }
}
