package com.mrt.model;

import java.util.Map;

import org.joda.time.LocalDate;

public class MovieTheaterReleaseDate
{
   private Movie mMovie;
   private Map<Theater, LocalDate> mTheaterReleaseDate;

   public MovieTheaterReleaseDate(Movie aMovie,
         Map<Theater, LocalDate> aTheaterReleaseDate)
   {
      super();
      mMovie = aMovie;
      mTheaterReleaseDate = aTheaterReleaseDate;
   }

   public Movie getMovie()
   {
      return mMovie;
   }

   public void setMovie(Movie aMovie)
   {
      mMovie = aMovie;
   }

   public Map<Theater, LocalDate> getTheaterReleaseDate()
   {
      return mTheaterReleaseDate;
   }

   public void setTheaterReleaseDate(Map<Theater, LocalDate> aTheaterReleaseDate)
   {
      mTheaterReleaseDate = aTheaterReleaseDate;
   }

}
