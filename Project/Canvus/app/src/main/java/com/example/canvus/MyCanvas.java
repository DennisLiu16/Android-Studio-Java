package com.example.canvus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/*ref : https://wiki.jikexueyuan.com/project/android-actual-combat-skills/canvas.html*/
public class MyCanvas extends View {
    private static final String TAG = "MyCanvas_Class";
    private Paint mPaint = null;
    private Bitmap mBitmap = null;
    private Canvas mCanvas = null;
    private float startX;
    private float startY;
    private int width = 1100;
    private int height = 2000;
    public int i;

    public MyCanvas(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        mBitmap = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.GRAY);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(6);
    }

    public void Clear()
    {
        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mCanvas.drawPaint(p);
        //mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        mCanvas.drawColor(Color.GRAY);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (mBitmap != null)
        {
            canvas.drawBitmap(mBitmap,0,0,mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float stopX = event.getX();
                float stopY = event.getY();

                /*log*/
                Log.e(TAG,"onTouchEvent-ACTION_MOVE\nstartX is "+startX+
                        " startY is "+startY+" stopX is "+stopX+ " stopY is "+stopY);
                /*draw*/
                mCanvas.drawLine(startX,startY,stopX,stopY,mPaint);

                /*reset*/
                startX = event.getX();
                startY = event.getY();
                invalidate();//call onDraw()
                break;
        }
        return true;
    }

}
