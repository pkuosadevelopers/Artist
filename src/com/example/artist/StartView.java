package com.example.artist;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class StartView extends GLSurfaceView 
{	
    public StartView(Context context, StartRenderer renderer) 
    {
        super(context); 
        mRenderer = renderer;
        setRenderer(mRenderer);
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float x = e.getX();
        switch (e.getAction()) 
        {
        case MotionEvent.ACTION_MOVE:
            float dx = x - mPreviousX;
            if(dx > 50)
            {
            	mOrientation = -1;
            	mRenderer.rotate = true;
            	requestRender();
            }
            else if(dx < -50)
            {
            	mOrientation = 1;
            	mRenderer.rotate = true;
            	requestRender();
            }
            break;
        case MotionEvent.ACTION_DOWN:
        	mPreviousX = x;
        	break;
        case MotionEvent.ACTION_UP:
        	if(Math.abs(mPreviousX - x) <= 5)
        	{
        		mEnter = true;
        		requestRender();
        	}
        	break;
        }
        return true;
    }
    
    public int mOrientation;//Ðý×ª·½Ïò
    public boolean mEnter = false;
    private float mPreviousX;
    private StartRenderer mRenderer;
}
