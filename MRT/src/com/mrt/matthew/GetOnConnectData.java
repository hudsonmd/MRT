package com.mrt.matthew;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class GetOnConnectData extends AsyncTask<String, Void, Void>
{
   private Context mContext;

   public GetOnConnectData(Context aContext)
   {
      mContext = aContext;
   }

   @Override
   protected Void doInBackground(String... params)
   {
      // TODO Auto-generated method stub
      final HttpURLConnection connection;
      String aSearchParameters = params[0];
      String cacheFolder = params[1];
      String startDate = params[2];
      final URL url;
      InputStream iS;
      InputStreamReader iSR;
      final BufferedReader in;
      try
      {
         Log.i("ConnectTask", "try");
         url =
               new URL("http://data.tmsapi.com/v1/movies/showings?"
                     + aSearchParameters + "&api_key=kzfwy87qdmzpx9u4uph8cn3m");
         Log.i("ConnectTask", "url");
         connection = (HttpURLConnection) url.openConnection();
         Log.i("ConnectTask", "openconnect");
         connection.connect();
         Log.i("ConnectTask", "connected");
         iS = new BufferedInputStream(connection.getInputStream());
         Log.i("ConnectTask", "inputstream");
         iSR = new InputStreamReader(iS);
         Log.i("ConnectTask", "inputstreamreader");
         in = new BufferedReader(iSR);
         String inputLine;
         Log.i("ConnectTask", "preWrite");
         FileOutputStream writer =
               mContext.openFileOutput(aSearchParameters + ".txt",
                     Context.MODE_PRIVATE);
         BufferedWriter fileWriter =
               new BufferedWriter(new OutputStreamWriter(writer));
         Log.i("ConnectTask", "postwrite");

         while ((inputLine = in.readLine()) != null)
         {
            fileWriter.write(inputLine);
         }
         fileWriter.flush();
         fileWriter.close();
         writer.flush();
         writer.close();
         in.close();
         connection.disconnect();
      }
      catch (Exception e)
      {

         Log.e(e.getMessage(), e.toString());
      }

      return null;
   }

}
