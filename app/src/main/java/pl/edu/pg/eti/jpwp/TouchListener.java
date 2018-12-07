package pl.edu.pg.eti.jpwp;

import android.view.MotionEvent;
import android.view.View;

public class TouchListener implements View.OnTouchListener {
    static MotionEvent eventBuffer;

    public TouchListener() {
        eventBuffer = null;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e) {
        eventBuffer = e;
        return false;
    }

}
