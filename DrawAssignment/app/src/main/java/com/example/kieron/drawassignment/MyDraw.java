package com.example.kieron.drawassignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Kieron on 30/12/2017.
 */

public class MyDraw extends View
{
    public int redVal, greenVal, blueVal;
    public float brushVal = 5;
    public boolean eraserEnabled = false;

    Canvas c;
    Bitmap bmp;
    Paint paint;
    float oldX, oldY;

    public MyDraw(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bmp = Bitmap.createBitmap(1080,1920,conf);

        paint = new Paint();
        paint.setAntiAlias(true);

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int R, G, B, A;
                float x, y;
                paint.setStrokeWidth(brushVal);
                paint.setStyle(Paint.Style.STROKE);

                if (eraserEnabled == false) paint.setColor(((255 << 24) + (redVal << 16) + (greenVal << 8) + (blueVal << 0)));
                else paint.setColor(Color.WHITE);

                int action = motionEvent.getActionMasked();

                c = new Canvas(bmp);

                x = motionEvent.getX();
                y = motionEvent.getY();

                switch(action)
                {
                    case MotionEvent.ACTION_DOWN:
                        oldX = x;
                        oldY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        c.drawLine(oldX, oldY, x, y, paint);
                        oldX = x;
                        oldY = y;
                        break;
                }
                Log.d("ACTION", "Touch State: " + action);

                invalidate();

                return true;
            }
        });
    }

    public void clearView()
    {
        c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas c)
    {
        super.onDraw(c);

        c.drawBitmap(bmp, 0,0,paint);
    }
}
