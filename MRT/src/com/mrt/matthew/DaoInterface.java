package com.mrt.matthew;

import java.util.List;

import org.joda.time.DateTime;

import com.mrt.model.Theater;


public interface DaoInterface {
	
	public List<LocalMovie> getMoviesPlayingInLocalTheaters(DateTime aStartDate, int aNumOfDays, int aZip, int aRadius) throws Exception;
	
	public List<Theater> getLocalTheaters(int aZip, int aRadius) throws Exception;
}
