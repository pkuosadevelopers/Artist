package com.example.artist;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class StartWorld 
{
	public StartWorld()
	{
        create();
        generate();
	}
	
	public void create()
	{
		//封面有4个顶点，这里还包括它自身 的倒影，所以一共要创建8个
		pVertices = new Vertex[4 * 2]; 
	
		// 初始化操作
		for (int i = 0; i < pVertices.length; i++) 
		{ 
			pVertices[i] = new Vertex(); 
		}
		
		// 设置封面初始化参数，宽度高度均为6.0f, 封面距离倒影的距离是0 
		// 如果需要显示宽屏的图片，可以在这里修改图片比例 
		float width = 15.0f, height = 15.0f, heightFromMirror = 1.0f; 
		
		//设置顶点的位置和纹理 
		pVertices[0].setp(-0.5f, 1, 0);
		pVertices[0].sett(0, 0);
		pVertices[1].setp(0.5f, 1, 0);
		pVertices[1].sett(1, 0);
		pVertices[2].setp(-0.5f, 0, 0); 
		pVertices[2].sett(0, 1);
		pVertices[3].setp(0.5f, 0, 0); 
		pVertices[3].sett(1, 1);
		
		//设置所有顶点的颜色 
		for (int i = 0; i < 4; i++) 
		{ 
			//设置顶点色 
			pVertices[i].setc(1.0f, 1.0f, 1.0f, 1.0f); 
			//设置顶点位置 
			pVertices[i].px = pVertices[i].px * width; 
			pVertices[i].py = pVertices[i].py * height + heightFromMirror; 
		} 
		
		//创建镜像倒影 
		for (int row = 0; row < 2; row++) 
		{ 
			//根据镜像的高度调整顶点色明暗 
			float dark = 0.5f * row;
            
			for (int col = 0; col < 2; col++) 
			{ 
				int offset = row * 2 + col;
				
				//将倒影封面的顶点垂直翻转 
				pVertices[offset + 4].set(pVertices[offset]); 
				pVertices[offset + 4].py = -pVertices[offset + 4].py; 
				
				//设置倒影封面几何体的顶点颜色
				pVertices[offset + 4].cx = dark; 
				pVertices[offset + 4].cy = dark;
				pVertices[offset + 4].cz = dark; 
			}
		}
			
		pIndices = new short[]{0,1,3,0,3,2, 6,7,5,6,5,4};
	}
	
	public void generate() 
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(8*4*4);
	    bb.order(ByteOrder.nativeOrder());
		mColorBuffer = bb.asFloatBuffer();
		
	    bb = ByteBuffer.allocateDirect(8*4*2);
	    bb.order(ByteOrder.nativeOrder());
		mTexBuffer = bb.asFloatBuffer();

	    bb = ByteBuffer.allocateDirect(8*4*3);
	    bb.order(ByteOrder.nativeOrder());
	    mVertexBuffer = bb.asFloatBuffer();

	    bb = ByteBuffer.allocateDirect(mIndexCount*2);
	    bb.order(ByteOrder.nativeOrder());
	    mIndexBuffer = bb.asShortBuffer();
	    
	    for(int i = 0; i < 8; i++)
		{
	    	pVertices[i].put(mVertexBuffer, mTexBuffer, mColorBuffer);
		}

		for(int i = 0; i < mIndexCount; i++)
		{
			mIndexBuffer.put(pIndices[i]);
		}
	}
	
	public void draw(GL10 gl)
    {
		mColorBuffer.position(0);
		mTexBuffer.position(0);
		mVertexBuffer.position(0);
		mIndexBuffer.position(0);

		gl.glFrontFace(GL10.GL_CW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glDrawElements(GL10.GL_TRIANGLES, mIndexCount, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);      
    }
	
	Vertex[] pVertices;
	short[] pIndices;
	private int mIndexCount = 12;
	private ShortBuffer mIndexBuffer;
	private FloatBuffer   mColorBuffer;
	private FloatBuffer   mVertexBuffer;
    private FloatBuffer mTexBuffer; 
}
