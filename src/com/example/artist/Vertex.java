package com.example.artist;

import java.nio.FloatBuffer;

public class Vertex 
{
	public float px, py, pz;
	public float cx, cy, cz, cw;
	public float tx, ty;
	
	public void setp(float x, float y, float z)
	{
		px = x;
		py = y;
		pz = z;
	}
	
	public void setc(float x, float y, float z, float w)
	{
		cx = x;
		cy = y;
		cz = z;
		cw = w;
	}
	
	public void sett(float x, float y)
	{
		tx = x;
		ty = y;
	}
	
	public void set(Vertex v)
	{
		px = v.px;
		py = v.py;
		pz = v.pz;
		
		cx = v.cx;
		cy = v.cy;
		cz = v.cz;
		cw = v.cw;
		
		tx = v.tx;
		ty = v.ty;
	}
	
	public void put(FloatBuffer vertexBuffer, FloatBuffer texBuffer, FloatBuffer colorBuffer) 
	{
        vertexBuffer.put(px);
        vertexBuffer.put(py);
        vertexBuffer.put(pz);

        texBuffer.put(tx);
        texBuffer.put(ty);
        
        colorBuffer.put(cx);
        colorBuffer.put(cy);
        colorBuffer.put(cz);
        colorBuffer.put(cw);
    }
}
