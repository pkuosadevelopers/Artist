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
		//������4�����㣬���ﻹ���������� �ĵ�Ӱ������һ��Ҫ����8��
		pVertices = new Vertex[4 * 2]; 
	
		// ��ʼ������
		for (int i = 0; i < pVertices.length; i++) 
		{ 
			pVertices[i] = new Vertex(); 
		}
		
		// ���÷����ʼ����������ȸ߶Ⱦ�Ϊ6.0f, ������뵹Ӱ�ľ�����0 
		// �����Ҫ��ʾ������ͼƬ�������������޸�ͼƬ���� 
		float width = 15.0f, height = 15.0f, heightFromMirror = 1.0f; 
		
		//���ö����λ�ú����� 
		pVertices[0].setp(-0.5f, 1, 0);
		pVertices[0].sett(0, 0);
		pVertices[1].setp(0.5f, 1, 0);
		pVertices[1].sett(1, 0);
		pVertices[2].setp(-0.5f, 0, 0); 
		pVertices[2].sett(0, 1);
		pVertices[3].setp(0.5f, 0, 0); 
		pVertices[3].sett(1, 1);
		
		//�������ж������ɫ 
		for (int i = 0; i < 4; i++) 
		{ 
			//���ö���ɫ 
			pVertices[i].setc(1.0f, 1.0f, 1.0f, 1.0f); 
			//���ö���λ�� 
			pVertices[i].px = pVertices[i].px * width; 
			pVertices[i].py = pVertices[i].py * height + heightFromMirror; 
		} 
		
		//��������Ӱ 
		for (int row = 0; row < 2; row++) 
		{ 
			//���ݾ���ĸ߶ȵ�������ɫ���� 
			float dark = 0.5f * row;
            
			for (int col = 0; col < 2; col++) 
			{ 
				int offset = row * 2 + col;
				
				//����Ӱ����Ķ��㴹ֱ��ת 
				pVertices[offset + 4].set(pVertices[offset]); 
				pVertices[offset + 4].py = -pVertices[offset + 4].py; 
				
				//���õ�Ӱ���漸����Ķ�����ɫ
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
