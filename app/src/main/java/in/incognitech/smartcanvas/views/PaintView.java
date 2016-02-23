package in.incognitech.smartcanvas.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by udit on 22/02/16.
 */
public class PaintView extends View {

    private class Point {
        public float x;
        public float y;
    }

    private Path path = new Path();
    private Paint paint = new Paint();

    public PaintView(Context context) {
        super(context);
        this.setup();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setup();
    }

    private void setup() {
        paint.setAntiAlias(true);
        paint.setColor(Color.DKGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(5f);

        this.setBackgroundColor(Color.WHITE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(event.getX(), event.getY());

                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                // nothing to do
                break;
            default:
                return false;
        }

        invalidate();

        return true;
    }

    public void clear() {
        path = new Path();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }

}
