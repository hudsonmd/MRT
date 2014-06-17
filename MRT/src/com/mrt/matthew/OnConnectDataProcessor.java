package com.mrt.matthew;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import com.mrt.model.Movie;
import com.mrt.model.Theater;

import android.util.Log;

public class OnConnectDataProcessor implements DataProcessorInterface
{
	DaoInterface mDao;
	DateTime mReferenceTime;
	
	public OnConnectDataProcessor(DaoInterface aDao){
		mDao = aDao;
		mReferenceTime = DateTime.now();
	}
	
	public OnConnectDataProcessor(DaoInterface aDao, boolean aTest)//File name
	{	
		mDao = aDao;
		if(aTest){
			mReferenceTime = new DateTime(2014,05,30,18,10,18);
		}
		else{
			mReferenceTime = DateTime.now();
		}
	}


	@Override
	public List<Theater> findTheaterByZip(final int aZip, final int aRadius) throws Exception {
		// TODO Auto-generated method stub
		return mDao.getLocalTheaters(aZip, aRadius);
	}

	@Override
	public DateTime findNextShowing(final String aMovieId, final int aZip, final int aRadius) throws Exception {
		// TODO Auto-generated method stub
		final List<LocalMovie> lMList = mDao.getMoviesPlayingInLocalTheaters(mReferenceTime, 3, aZip, aRadius);
		LocalMovie selection = null;
		for(final LocalMovie lM : lMList){
			if(lM.getMovie().getMovieId().equals(aMovieId)){
				selection = lM;
			}
		}
		
		DateTime nextTime;
		if(selection.equals(null)){
			nextTime = null;
		}
		else{
			final List<Theater> theaterList = selection.getTheaterList();
			nextTime = mReferenceTime.plusYears(10);
			for(final Theater theater : theaterList){
				for(final DateTime lDT : theater.getShowtimesForMovie(new Movie(aMovieId, null, null))){
					if(lDT.isBefore(nextTime)&&lDT.isAfter(mReferenceTime)){
						nextTime = lDT;
					}
				}
			}
			if(nextTime.isAfter(mReferenceTime.plusYears(9))){
				nextTime = null;
			}
		}
		return nextTime;
	}

	@Override
	public DateTime findNextShowingInTheater(final String aMovieId, final String aTheaterId, final int aZip) throws Exception {
		final List<LocalMovie> lMList = mDao.getMoviesPlayingInLocalTheaters(mReferenceTime, 3, aZip, 10);
		LocalMovie selection = null;
		for(final LocalMovie lM : lMList){
			if(lM.getMovie().getMovieId().equals(aMovieId)){
				selection = lM;
			}
		}
		DateTime nextTime;
		if(!selection.equals(null)){
			final Theater theater = selection.getSpecificTheater(aTheaterId);
			nextTime = new DateTime(3000, 12, 31, 9, 9);
			if(!theater.getShowtimesForMovie(new Movie(aMovieId, null, null)).isEmpty()){
				for(final DateTime lDT : theater.getShowtimesForMovie(new Movie(aMovieId, null, null))){
					if(lDT.isBefore(nextTime)&&lDT.isAfter(mReferenceTime)){
						nextTime = lDT;
					}
				}
				if(nextTime.isEqual(new DateTime(3000, 12, 31, 9, 9))){
					nextTime = null;
				}
			}
			else{
				nextTime = null;
			}
		}
		else{
			nextTime = null;
		}
		return nextTime;
	}

	@Override
	public List<Movie> findMoviesPlayingSoon(final int aDays, final int aZip, final int aRadius) throws Exception {
		// TODO Auto-generated method stub
		final List<LocalMovie> localMovies = mDao.getMoviesPlayingInLocalTheaters(mReferenceTime, aDays, aZip, aRadius);
		final List<Movie> movies = new ArrayList<Movie>();
		for(final LocalMovie lM : localMovies){
			movies.add(lM.getMovie());
		}
		Log.i("parser", movies.toString());
		return movies;
	}


	@Override
	public Movie getMovieInfo(final String aMID, final int aZip) throws Exception {
		Movie mov = null;
		final List<LocalMovie> lMList = mDao.getMoviesPlayingInLocalTheaters(mReferenceTime, 3, aZip, 10);
		for(final LocalMovie lM : lMList){
			if(lM.getMovie().getMovieId().equals(aMID)){
				mov = lM.getMovie();
			}
		}
		return mov;
	}
	
	public List<LocalMovie> findLocalMoviesByName(String aMovieName, final int aZip) throws Exception{
		List<LocalMovie> matches = new ArrayList<LocalMovie>();
		final List<LocalMovie> lMList = mDao.getMoviesPlayingInLocalTheaters(mReferenceTime, 3, aZip, 10);
		for(final LocalMovie lM : lMList){
			if(lM.getMovie().getMovieName().contains(aMovieName)){
				matches.add(lM);
			}
		}
		return matches;
	}

}
