package com.mrt.model;

import org.joda.time.LocalDate;

public class Movie
{

   private final String mMovieName;
   private final String mMovieId;
   private final LocalDate mReleaseDate;

   public Movie(String aMovieId, String aMovieName)
   {
      mMovieId = aMovieId;
      mMovieName = aMovieName;
      mReleaseDate = null;
   }

   public Movie(String aMovieId, String aMovieName, String aReleaseDate)
   {
      mMovieId = aMovieId;
      mMovieName = aMovieName;
      if (aReleaseDate != null)
      {
         final int rYear = Integer.parseInt(aReleaseDate.substring(0, 4));
         final int rMonth = Integer.parseInt(aReleaseDate.substring(5, 7));
         final int rDay = Integer.parseInt(aReleaseDate.substring(8));
         mReleaseDate = new LocalDate(rYear, rMonth, rDay);
      }
      else
      {
         mReleaseDate = null;
      }
   }
   
   

   public String getMovieName()
   {
      return mMovieName;
   }

   public String getMovieId()
   {
      return mMovieId;
   }

   public LocalDate getReleaseDate()
   {
      return mReleaseDate;
   }
   
   

   @Override
   public String toString()
   {
      return mMovieName;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((mMovieId == null) ? 0 : mMovieId.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Movie other = (Movie) obj;
      if (mMovieId == null)
      {
         if (other.mMovieId != null)
            return false;
      }
      else if (!mMovieId.equals(other.getMovieId()))
         return false;
      return true;
   }

}
