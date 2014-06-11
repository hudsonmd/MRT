package com.mrt.matthew;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;

import android.content.Context;
import android.util.Log;

import com.mrt.model.Theater;

//http://www.tutorialspoint.com/json/json_java_example.htm

public class OnConnectJSONDao implements DaoInterface
{

   private URLConnection mConnection;
   private final String mCacheFolder;
   private final int mExpireHours;
   private boolean mTest = false;
   private DateTime mStartDate = DateTime.now();
   Map<String, Theater> mTheaterMap = new HashMap<String, Theater>();
   List<LocalMovie> mLMovList = new ArrayList<LocalMovie>();
   String mSearchParameters = "";

   private Context mContext;

   public OnConnectJSONDao(Context aContext)
   {
      mCacheFolder = "cache/OnConnectJSONCache/";
      mExpireHours = 1;
      mContext = aContext;
   }

   public OnConnectJSONDao(boolean test)
   {
      if (test)
      {
         mCacheFolder = "cache/OnConnectJSONCacheTest/";
         mExpireHours = 9000;
         mTest = true;
         mStartDate = new DateTime(2014, 4, 9, 22, 21, 55);
      }
      else
      {
         mCacheFolder = "cache/OnConnectJSONCache/";
         mExpireHours = 1;
      }
   }

   private void ObtainOnConnectData(final String aSearchParameters)
         throws Exception
   {
      mSearchParameters = aSearchParameters;

      File file = mContext.getFileStreamPath(mSearchParameters+".txt");
      Log.i("checkforfile", file.exists()+"");
      if(!file.exists()){
	   Log.i("staticfile", "filenotfound");
    	  GetOnConnectData connect = new GetOnConnectData(mContext);
	
	      connect.execute(mSearchParameters, mCacheFolder, mStartDate.toString())
	            .get(60, TimeUnit.SECONDS);
      }
      ReadOnConnectData read = new ReadOnConnectData(mContext);
      Object[] data = read.execute(mSearchParameters).get(60, TimeUnit.SECONDS);
      mTheaterMap = (Map<String, Theater>) data[0];
      mLMovList = (List<LocalMovie>) data[1];
   }


   @Override
   public List<LocalMovie> getMoviesPlayingInLocalTheaters(DateTime aStartDate,
         int aNumOfDays, int aZip, int aRadius) throws Exception
   {
      if (mTest)
      {
         aStartDate = mStartDate;
      }
      ObtainOnConnectData("startDate=" + aStartDate.getYear() + "-"
            + aStartDate.getMonthOfYear() + "-" + aStartDate.getDayOfMonth()
            + "&numDays=" + aNumOfDays + "&zip=" + aZip + "&aRadius=" + aRadius);
      Log.i("json", mLMovList.toString());
      Log.i("json", mLMovList.get(0).getMovie().getMovieName());
      return mLMovList;
   }

   @Override
   public List<Theater> getLocalTheaters(int aZip, int aRadius)
         throws Exception
   {
      ObtainOnConnectData("startDate=" + mStartDate.getYear() + "-"
            + mStartDate.getMonthOfYear() + "-" + mStartDate.getDayOfMonth()
            + "&numDays=" + 3 + "&zip=" + aZip + "&aRadius=" + aRadius);
      List<Theater> li = new ArrayList<Theater>();
      for (String s : mTheaterMap.keySet())
      {
         li.add(mTheaterMap.get(s));
         Log.i("loop", s);
         Log.i("loop2", mTheaterMap.get(s).getId());
      }
      Log.i("theat", mTheaterMap.toString());
      Log.i("getTheat", "" + li.size());
      return li;
   }


   protected static DateTime stringToDateTime(final String argS)
   {
      final int year = Integer.parseInt(argS.substring(1, 4)) + 2000;
      final int month = Integer.parseInt(argS.substring(5, 7));
      final int day = Integer.parseInt(argS.substring(8, 10));
      final int hour = Integer.parseInt(argS.substring(11, 13));
      final int minute = Integer.parseInt(argS.substring(14));
      return new DateTime(year, month, day, hour, minute);
   }

public URLConnection getConnection() {
	return mConnection;
}

public void setConnection(URLConnection aConnection) {
	mConnection = aConnection;
}

public boolean isTest() {
	return mTest;
}

public void setTest(boolean aTest) {
	mTest = aTest;
}

public DateTime getStartDate() {
	return mStartDate;
}

public void setStartDate(DateTime aStartDate) {
	mStartDate = aStartDate;
}

public Map<String, Theater> getTheaterMap() {
	return mTheaterMap;
}

public void setTheaterMap(Map<String, Theater> aTheaterMap) {
	mTheaterMap = aTheaterMap;
}

public List<LocalMovie> getLMovList() {
	return mLMovList;
}

public void setLMovList(List<LocalMovie> aLMovList) {
	mLMovList = aLMovList;
}

public String getSearchParameters() {
	return mSearchParameters;
}

public void setSearchParameters(String aSearchParameters) {
	mSearchParameters = aSearchParameters;
}

public Context getContext() {
	return mContext;
}

public void setContext(Context aContext) {
	mContext = aContext;
}

public String getCacheFolder() {
	return mCacheFolder;
}

public int getExpireHours() {
	return mExpireHours;
}

}
