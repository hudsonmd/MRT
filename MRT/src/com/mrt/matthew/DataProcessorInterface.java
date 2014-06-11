package com.mrt.matthew;

import java.util.List;
import org.joda.time.DateTime;

import com.mrt.model.Movie;
import com.mrt.model.Theater;


public interface DataProcessorInterface {

	public List<Theater> findTheaterByZip(int aZip, int aRadius) throws Exception;
	
	public DateTime findNextShowing(String aMovieId, int aZip, int aRadius) throws Exception;
	
	public DateTime findNextShowingInTheater(String aMovieId, String aTheaterId, int aZip) throws Exception;
	
	public List<Movie> findMoviesPlayingSoon(int aDays, int aZip, int aRadius) throws Exception;
	
	public Movie getMovieInfo(String aMID, int aZip) throws Exception;
		
}
