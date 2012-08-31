package com.example.artist;

import android.graphics.Movie;

public class Animator 
{	
	public Animator(Movie movie, float w, float h, long start)
	{
		mMovie = movie;
		width =w;
		height = h;
		mMovieStart = start;
	}
	
	public Movie getMovie() {
		return mMovie;
	}
	public float getWidth() {
		return width;
	}
	public float getHeight() {
		return height;
	}
	public long getMovieStart() {
		return mMovieStart;
	}

	private Movie mMovie;//动画
	private float width;//位置信息
	private float height;
	private long mMovieStart;//开始时间	
}
