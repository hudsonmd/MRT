package com.mrt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;


public class Theater
{
   private final String mId;
   private final String mName;
   private Map<Movie, List<DateTime>> mMovieShowtimes;
   private final int mZip;

   public Theater(String aId, int aZip)
   {
      super();
      mId = aId;
      mName = null;
      mMovieShowtimes = new HashMap<Movie, List<DateTime>>();
      mZip = aZip;
   }
   
   public Theater(String aId, String aName, int aZip)
   {
      super();
      mId = aId;
      mName = aName;
      mMovieShowtimes = new HashMap<Movie, List<DateTime>>();
      mZip = aZip;
   }

   public void addShowtimeToTheater(final Movie aMovie,
         final DateTime aShowtime)
   {
      if (!mMovieShowtimes.containsKey(aMovie))
      {
         mMovieShowtimes.put(aMovie, new ArrayList<DateTime>());
      }
      mMovieShowtimes.get(aMovie).add(aShowtime);
   }

   public List<DateTime> getShowtimesForMovie(final Movie aMovie)
   {
      return mMovieShowtimes.get(aMovie);
   }
   
   public Set<Movie> getAllMovies()
   {
      return mMovieShowtimes.keySet();
   }

   @Override
   public String toString()
   {
      return mName;
   }

   public String getId()
   {
      return mId;
   }

   public String getName()
   {
      return mName;
   }

}
