package com.mrt.matthew;

import java.util.ArrayList;
import java.util.List;

import com.mrt.model.Movie;
import com.mrt.model.Theater;


public class LocalMovie {
	private final Movie mMovie;
	private final List<Theater> mTheaterList;
	public LocalMovie(final Movie aMovie, final List<Theater> aTheaterList) {
		super();
		this.mMovie = aMovie;
		mTheaterList = aTheaterList;
	}
	public Movie getMovie() {
		return mMovie;
	}
	public List<Theater> getTheaterList() {
		return mTheaterList;
	}
	
	public Theater getSpecificTheater(final String aTheaterId){
		Theater specTheater = null;
		for(final Theater theater : mTheaterList){
			if(theater.getId().equals(aTheaterId)){
				specTheater = theater;
			}
		}
		return specTheater;
	}
	
	
}
