package com.example.artist;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;

public class StartRenderer implements GLSurfaceView.Renderer 
{
    public interface AnimationCallback 
    {
        void animate();
    }
    
    public StartRenderer(StartWorld world, AnimationCallback callback, Context context) 
    {
    	mContext = context;
        mWorld = world;
        mCallback = callback;
    }
    
    public void onDrawFrame(GL10 gl) 
    {
        if (mCallback != null) 
        {
            mCallback.animate();
        }

       /*
        * Usually, the first thing one might want to do is to clear
        * the screen. The most efficient way of doing this is to use
        * glClear(). However we must make sure to set the scissor
        * correctly first. The scissor is always specified in window
        * coordinates:
        */
       //gl.glDisable(GL10.GL_DITHER);
       gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
               GL10.GL_MODULATE);
       
       gl.glClearColor(0.0f,0.0f,0.0f,1);
       gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

       /*
        * Now we're ready to draw some 3D object
        */

       gl.glMatrixMode(GL10.GL_MODELVIEW);
       gl.glLoadIdentity();
       GLU.gluLookAt(gl, 0, 20, 40, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
       

       gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
       gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
       gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
       gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    
       gl.glEnable(GL10.GL_DEPTH_TEST);
       gl.glActiveTexture(GL10.GL_TEXTURE0);
  
       for(int i = 0; i < 5; i++)
       {
    	   gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[i]);
           gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                   GL10.GL_REPEAT);
           gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                   GL10.GL_REPEAT);
           
    	   gl.glPushMatrix();
    	   gl.glTranslatef((float)(-radius * Math.sin(angle[i]*Math.PI/180)), 
        		   height[i], (float)(radius * (Math.cos(angle[i]*Math.PI/180) - 1)));
           mWorld.draw(gl);
           gl.glPopMatrix();
       }
   }

   public void onSurfaceChanged(GL10 gl, int width, int height) 
   {
       gl.glViewport(0, 0, width, height);
      
       /*
        * Set our projection matrix. This doesn't have to be done
        * each time we draw, but usually a new projection needs to be set
        * when the viewport is resized.
        */

       float ratio = (float)width / height;
       gl.glMatrixMode(GL10.GL_PROJECTION);
       gl.glLoadIdentity();
       gl.glFrustumf(-ratio, ratio, -1, 1, 2, 150);

       /*
        * By default, OpenGL enables features that improve quality
        * but reduce performance. One might want to tweak that
        * especially on software renderer.
        */
       gl.glDisable(GL10.GL_DITHER);
       gl.glActiveTexture(GL10.GL_TEXTURE0);
   }

   public void onSurfaceCreated(GL10 gl, EGLConfig config) 
   {
   	/*
        * By default, OpenGL enables features that improve quality
        * but reduce performance. One might want to tweak that
        * especially on software renderer.
        */
       gl.glDisable(GL10.GL_DITHER);

       /*
        * Some one-time OpenGL initialization can be made here
        * probably based on features of this particular context
        */
       gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
               GL10.GL_NICEST);

       gl.glShadeModel(GL10.GL_SMOOTH);
       gl.glEnable(GL10.GL_DEPTH_TEST);
       gl.glEnable(GL10.GL_TEXTURE_2D);

       /*
        * Create our texture. This has to be done each time the
        * surface is created.
        */

       textures = new int[5];
       gl.glGenTextures(5, textures, 0);
       loadTexture(gl, textures[0], R.drawable.paint1);
       loadTexture(gl, textures[1], R.drawable.paint2);
       loadTexture(gl, textures[2], R.drawable.paint3);
       loadTexture(gl, textures[3], R.drawable.paint4);
       loadTexture(gl, textures[4], R.drawable.paint5);
   }
   
   private void loadTexture(GL10 gl, int textureID, int id)
   {
	   gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);

       gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
               GL10.GL_LINEAR);
       gl.glTexParameterf(GL10.GL_TEXTURE_2D,
               GL10.GL_TEXTURE_MAG_FILTER,
               GL10.GL_LINEAR);

       gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
               GL10.GL_CLAMP_TO_EDGE);
       gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
               GL10.GL_CLAMP_TO_EDGE);

       gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
               GL10.GL_REPLACE);

       InputStream is = mContext.getResources()
               .openRawResource(id);
       Bitmap bitmap;
       try {
           bitmap = BitmapFactory.decodeStream(is);
       } finally {
           try {
               is.close();
           } catch(IOException e) {
               // Ignore.
           }
       }

       GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
       bitmap.recycle();
   }
   
   public int[] angle = {0, 288, 216, 144, 72};
   public int speed = 18;
   public float radius = 33;
   public int[] height = {0, 20, 20, 20, 20};
   public int[] textures;
   public boolean rotate = false;
   private Context mContext;
   private StartWorld mWorld;
   private AnimationCallback mCallback;
}
