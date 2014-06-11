package com.mrt.matthew;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mrt.model.Movie;
import com.mrt.model.Theater;

public class ReadOnConnectData extends
      AsyncTask<String, Void, Object[]>
{
   private Context mContext;
   private Map<String, Theater> mTheaterMap;
   private List<LocalMovie> mLMovList;
   public ReadOnConnectData(Context aContext)
   {
      mContext = aContext.getApplicationContext();
      mTheaterMap = new HashMap<String, Theater>();
      mLMovList = new ArrayList<LocalMovie>();
   }

   //returns mTheaterMap, mLMovList
   @Override
   protected Object[] doInBackground(String... aSearchParameters)
   {
      StringBuilder sb = new StringBuilder();
      //gets zip for theaters in search
      String zipString = aSearchParameters[0];
      int zipStart = zipString.indexOf("zip=")+4;
      zipString = zipString.substring(zipStart, zipString.indexOf("&", zipStart)); 
      int zip = Integer.parseInt(zipString);
      try
      {
         Log.i("ReadTask", "pre inputstream");
         FileInputStream inputStream =
               mContext.openFileInput(aSearchParameters[0]
                     .toString() + ".txt");
         Log.i("ReadTask", "inputsstream");
         BufferedReader fileReader =
               new BufferedReader(new InputStreamReader(inputStream));
         Log.i("ReadTask", "filereader");
         String line;
         while ((line = fileReader.readLine()) != null)
         {
            sb.append(line);
         }
      }
      catch (Exception e)
      {
         Log.e(e.getMessage(), e.toString());
      }
      JSONParser parser = new JSONParser();
      Object obj = null;
      try
      {
         obj = parser.parse(sb.toString());
      }
      catch (ParseException e)
      {
         Log.i("ReadTask", e.toString());
      }
      JSONArray array = (JSONArray) obj;
      // iterates through all movies and their data
      Log.i("ReadTask", sb.toString());
      for (Object li : array)
      {
         // creates movie
         String releaseDate = (String) ((JSONObject) li).get("releaseDate");
         String title = (String) ((JSONObject) li).get("title");
         String movieId = (String) ((JSONObject) li).get("tmsId");
         Movie mov = new Movie(movieId, title, releaseDate);
         List<Theater> theaterList = new ArrayList<Theater>();
         // creates theater and theaterList for movie
         for (Object o : ((JSONArray) ((JSONObject) li).get("showtimes")))
         {
            DateTime showtime =
                  OnConnectJSONDao.stringToDateTime((String) ((JSONObject) o)
                        .get("dateTime"));
            String theaterId =
                  (String) ((JSONObject) ((JSONObject) o).get("theatre"))
                        .get("id");
            String theaterName =
                  (String) ((JSONObject) ((JSONObject) o).get("theatre"))
                        .get("name");
            // adds theater to map
            if (mTheaterMap.containsKey(theaterId))
            {
               mTheaterMap.get(theaterId).addShowtimeToTheater(mov,
                     showtime);
            }
            else
            {
               Theater thea = new Theater(theaterId, theaterName, zip);
               thea.addShowtimeToTheater(mov, showtime);
               mTheaterMap.put(theaterId, thea);

            }
            // adds theater to list to add to localmovie
            if (!theaterList.contains(mTheaterMap.get(theaterId)))
            {
               theaterList.add(mTheaterMap.get(theaterId));
            }
         }
         mLMovList.add(new LocalMovie(mov, theaterList));
      }
      Log.i("lmList", mLMovList.get(0).getMovie().getMovieName());
      Log.i("theaterList", mTheaterMap.toString());
      Log.i("ReadTask", "complete");
      Object[] ret = {mTheaterMap, mLMovList};
      return ret;
   }

}
