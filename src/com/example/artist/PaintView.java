package com.example.artist;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.os.SystemClock;
import android.util.AttributeSet;

public class PaintView extends View {
        
        private Bitmap  mForeBitmap;
        private Bitmap  mBackBitmap;
		private Canvas  mCanvas;
        private Path    mPath;
        private Paint   mBitmapPaint;
        private Paint   mPaint;
        private int     mShape;
        private int     mBkColor;
        private int     mCount;
        private boolean mEraser;
        private boolean mPicture;
        private List<Animator> 	movieList;

		public PaintView(Context c) {
            super(c);
            initialize();
        }
		
		public PaintView(Context context, AttributeSet attrs)
	    {
	        super(context, attrs);
	        initialize();
	    }

	    public PaintView(Context context, AttributeSet attrs, int defStyle)
	    {
	        super(context, attrs, defStyle);
	        initialize();
	    }
	    
	    private void initialize()
	    {
	    	mForeBitmap = Bitmap.createBitmap(800, 480, Bitmap.Config.ARGB_8888);
	    	mBackBitmap = Bitmap.createBitmap(800, 480, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mForeBitmap);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            movieList = new ArrayList<Animator>();
            mShape = 1;
            mCount = 0;
            mBkColor = Color.WHITE;
            mEraser = false;
            mPicture = false;
            
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setColor(0xFFFF0000);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setStrokeCap(Paint.Cap.ROUND);
            mPaint.setStrokeWidth(12);
         
	    }

	    public void setmPicture(boolean mPicture) {
			this.mPicture = mPicture;
		}

		public void setmEraser(boolean mEraser) {
			this.mEraser = mEraser;
		}
	    
		public Paint getmPaint() {
			return mPaint;
		}
		
		public void setmShape(int mShape) {
			this.mShape = mShape;
		}
		
		public void clear(){
			mForeBitmap.recycle();
			mForeBitmap = Bitmap.createBitmap(800, 480, Bitmap.Config.ARGB_8888);
			mCanvas.setBitmap(mForeBitmap);
			movieList.clear();
			invalidate();
		}
		
		public void changeBk(int color)
		{
			mBkColor = color;
			invalidate();
		}
		
		public void setForeBitmap(Bitmap bmp)
		{
	        mForeBitmap.recycle();
	        mForeBitmap = BitmapUtil.duplicateBitmap(bmp);
			mCanvas.setBitmap(mForeBitmap);
			invalidate();
		}
		
		public void setBackBitmap(Bitmap bmp)
		{
			int dstWidth = bmp.getWidth();
	        int dstHeight = bmp.getHeight();
	        mBackBitmap.recycle();
	        mBackBitmap = Bitmap.createScaledBitmap(bmp, dstWidth, dstHeight, false);
			invalidate();
		}
		
		public Bitmap getCanvasSnapshot()
	    {
	        setDrawingCacheEnabled(true);
	        buildDrawingCache(true);
	        Bitmap bmp = getDrawingCache(true);
	        
	        if (null == bmp)
	        {
	            android.util.Log.d("leehong2", "getCanvasSnapshot getDrawingCache == null");
	        }
	        
	        return BitmapUtil.duplicateBitmap(bmp);
	    }
		
        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
        	
        	if(!mPicture)
        		canvas.drawColor(mBkColor);
        	else
        		canvas.drawBitmap(mBackBitmap, 0, 0, mBitmapPaint);
        	
            canvas.drawBitmap(mForeBitmap, 0, 0, mBitmapPaint);
            
            if(!mEraser)
            	canvas.drawPath(mPath, mPaint);
            
            for(Animator ani : movieList)
            {
            	long now = SystemClock.uptimeMillis();
            	int relTime = (int)((now - ani.getMovieStart()) % 1000);
                ani.getMovie().setTime(relTime);
                ani.getMovie().draw(canvas, ani.getWidth(), ani.getHeight());
                invalidate();
            }
        }
        
        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;
        
        private void touch_start(float x, float y) {
            switch(mShape)
            {
            case 1:
            	mPath.reset();
            	mPath.moveTo(x, y);
            	break;
            case 5:
            	mCount++;
            	Bitmap bitmap;
            	InputStream is = null;
            	
            	switch(mCount%4)
            	{
            	case 0:
            		is = this.getContext().getResources().openRawResource(R.drawable.fish);
            		break;
            	case 1:
            		is = this.getContext().getResources().openRawResource(R.drawable.fox);
            		break;
            	case 2:
            		is = this.getContext().getResources().openRawResource(R.drawable.frog);
            		break;
            	case 3:
            		is = this.getContext().getResources().openRawResource(R.drawable.flag);
            		Animator bear = new Animator(Movie.decodeStream(is), x - 64, y - 64, SystemClock.uptimeMillis());
            		movieList.add(bear);
            		break;
            	}
            	
            	if(mCount%4 != 3)
            	{
            		bitmap = BitmapFactory.decodeStream(is);
                	try {
    					is.close();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}	
    				mCanvas.drawBitmap(bitmap, x - 50, y - 50, mBitmapPaint);
            	}
            }
            mX = x;
            mY = y;
        }
        
        private void touch_move(float x, float y) {
        	switch(mShape)
        	{
        	case 1:
        		float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
                    mX = x;
                    mY = y;
                }break;
        	case 2:
        		mPath.reset();
        		mPath.moveTo(mX, mY);
        		mPath.lineTo(x, y);
        		break;
        	case 3:
        		mPath.reset();
        		RectF mRect = new RectF();
        		mRect.set(mX, mY, x, y);
        		mPath.addOval(mRect, Path.Direction.CW);
        		break;
        	case 4:
        		mPath.reset();
        		RectF mRect1 = new RectF();
        		mRect1.set(mX, mY, x, y);
        		mPath.addRect(mRect1, Path.Direction.CW);
        		break;
        	}
            
        }
        
        private void touch_up() {
        	switch(mShape)
        	{
        	case 1:
        		mPath.lineTo(mX, mY);
        		break;
        	}
        	mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }
        
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            if(mEraser)
            {
            	mCanvas.drawPath(mPath, mPaint);
                invalidate();
            }
            return true;
        }
    }