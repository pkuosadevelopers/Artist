package com.example.artist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class StartActivity extends Activity implements StartRenderer.AnimationCallback 
{
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	 super.onCreate(savedInstanceState);
    	 
         // We don't need a title either.
         requestWindowFeature(Window.FEATURE_NO_TITLE);

         mWorld = new StartWorld();
         mRenderer = new StartRenderer(mWorld, this, this);
         mView = new StartView(getApplication(), mRenderer);
         setContentView(mView);
         //setContentView(R.layout.start);
         //mView = (StartView)this.findViewById(R.id.StartView);
         //mView.setRenderer(mRenderer);
         mView.requestFocus();
         mView.setFocusableInTouchMode(true);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mView.onPause();
    }
    
    public void animate() 
    {
    	if(!mView.mEnter && mRenderer.height[1] >= 0)
    		for(int i = 1; i < 5; i++)
    			mRenderer.height[i]--;
    	
    	if(mView.mEnter)
    	{
    		for(int i = 0; i < 5; i++)
    		{
    			if(i != mChoice - 1)
    			{
    				mRenderer.height[i] += 2;
    				if(mRenderer.height[i] > 50)
            			mActivity = mChoice; 
    			}
    		}
    	}
    	
    	if(!mView.mEnter && mRenderer.rotate && mRenderer.height[1] <= 0)
    	{
    		if(flag)
    		{
    			for(int i = 0; i < 5; i++)
    				dest[i] = mRenderer.angle[i] + 72 * mView.mOrientation;
    			
    			mChoice = mChoice + mView.mOrientation;
    			
    			if(mChoice == 6)
    				mChoice = 1;
    			else if(mChoice == 0)
    				mChoice = 5;
    			
    			flag = false;
    		}
    		
    		for(int i = 0; i < 5; i++)
    			mRenderer.angle[i] += 72/mRenderer.speed * mView.mOrientation;
    		
    		if(mRenderer.angle[0] == dest[0])
    		{
    			mRenderer.rotate = false;
    			flag = true;
    		}
    	}
    	
    	if(mActivity != 0)
    	{
    		Intent intent = new Intent(this, SimplePaintActivity.class);
    		intent.putExtra("choice", mChoice);
            startActivity(intent);
            finish();
    	}
    }
   
    int[] dest = {0, 0, 0, 0, 0};
    boolean flag = true;
    int mActivity = 0;
    int mChoice = 1;
    StartWorld mWorld;
    StartView mView;
    StartRenderer mRenderer;
}