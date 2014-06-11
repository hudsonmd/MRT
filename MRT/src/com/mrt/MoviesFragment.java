package com.mrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.LocalDate;

import com.movieproject.R;
import com.mrt.model.Movie;
import com.mrt.model.MovieTheaterReleaseDate;
import com.mrt.model.Theater;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MoviesFragment extends Fragment
{
   List<Movie> mMovies;
   Map<Movie, MovieTheaterReleaseDate> mMoviesReleaseDate;
   private ArrayAdapter<Movie> mAdapter;
   private SharedPreferences mSavedValues = null;
   boolean mLoaded = false;

   public MoviesFragment()
   {
      mMovies = new ArrayList<Movie>();
   }

   @Override
   public View onCreateView(LayoutInflater aInflater, ViewGroup aContainer,
         Bundle aSavedInstanceState)
   {
      // Inflate the layout for this fragment
      View view = aInflater.inflate(R.layout.fragment_movie, aContainer, false);
      getActivity().getActionBar().setTitle(R.string.app_name);
      Context context = getActivity();
      mSavedValues = context.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

      // Create on click handler
      Button button = (Button) view.findViewById(R.id.movieSearchButton);
      button.setOnClickListener(new OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            EditText editText =
                  (EditText) getView().findViewById(R.id.movieSearchText);
            mMoviesReleaseDate = getSavedMovies();
            populateMoviesList(editText.getText().toString());
            InputMethodManager mgr =
                  (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
         }
      });

      OnItemClickListener listViewClickListener = new OnItemClickListener()
      {
         public void onItemClick(AdapterView<?> adapterView, View v,
               int position,
               long id)
         {
            Movie selectedMovie =
                  (Movie) adapterView.getItemAtPosition(position);
            AlertDialog.Builder builder =
                  new AlertDialog.Builder(getActivity());

            StringBuilder strbuilder = new StringBuilder();
            for (Theater theater : mMoviesReleaseDate.get(selectedMovie)
                  .getTheaterReleaseDate().keySet())
            {
               strbuilder.append("Theater: "
                     + theater.getName()
                     + ", ReleaseDate: "
                     + mMoviesReleaseDate.get(selectedMovie)
                           .getTheaterReleaseDate().get(theater).toString());

            }
            builder.setMessage(strbuilder.toString());

            // Create the AlertDialog
            AlertDialog dialog = builder.create();

            dialog.show();

         }
      };

      ArrayAdapter<Movie> adapter = new ArrayAdapter<Movie>(getActivity(),
            android.R.layout.simple_list_item_1, mMovies);

      ListView movieList = (ListView) view.findViewById(R.id.movieList);
      movieList.setAdapter(adapter);
      movieList.setOnItemClickListener(listViewClickListener);
      mAdapter = adapter;
      mMoviesReleaseDate = getSavedMovies();
      populateMoviesList(null);
      mLoaded = true;
      getActivity().getActionBar().setTitle(R.string.app_name);
      getActivity().getActionBar().setDisplayShowTitleEnabled(true);

      return view;
   }

   @Override
   public void setUserVisibleHint(boolean isVisibleToUser)
   {
      if (mLoaded)
      {
         super.setUserVisibleHint(isVisibleToUser);

         // Make sure that we are currently not visible
         if (!this.isVisible())
         {
            // If we are becoming visible, then...
            if (isVisibleToUser)
            {
               mMoviesReleaseDate = getSavedMovies();
               populateMoviesList(null);
            }
         }
      }
   }

   private void populateMoviesList(String aSearchText)
   {
      mAdapter.clear();
      mMovies = new ArrayList<Movie>();
      // Call Matthew's code for movie list

      for (Movie movie : mMoviesReleaseDate.keySet())
      {
         if (aSearchText == null || movie.getMovieName().toLowerCase().contains(aSearchText.toLowerCase()))
         {
            mMovies.add(movie);
         }
      }

      mAdapter.addAll(mMovies);
      mAdapter.notifyDataSetChanged();
   }

   private List<Theater> getSavedTheaters()
   {
      List<Theater> savedTheaters = new ArrayList<Theater>();
      for (String id : mSavedValues.getAll().keySet())
      {
         if (!id.contains("movies"))
         {
            Set<String> values = mSavedValues.getStringSet(id + "movies", null);
            Iterator<String> it = null;
            if(!(values==null)){
            	it = values.iterator();
            //change zeros to zipcode
            Theater theater = new Theater(id, mSavedValues.getString(id, null), 00000);
            while (it.hasNext())
            {
               String movieString = it.next();
               String split[] = movieString.split(",");
               Movie movie = new Movie(split[0], split[1], split[2]);
               theater.addShowtimeToTheater(movie, null);
            }
            savedTheaters.add(theater);
            }
         }
      }
      return savedTheaters;
   }

   private Map<Movie, MovieTheaterReleaseDate> getSavedMovies()
   {
      List<Theater> savedTheaters = getSavedTheaters();
      Map<Movie, MovieTheaterReleaseDate> movies =
            new HashMap<Movie, MovieTheaterReleaseDate>();
      for (Theater theater : savedTheaters)
      {
         for (Movie movie : theater.getAllMovies())
         {
            if (!movies.containsKey(movie))
            {
               movies.put(movie, new MovieTheaterReleaseDate(movie,
                     new HashMap<Theater, LocalDate>()));
            }
            movies.get(movie).getTheaterReleaseDate()
                  .put(theater, movie.getReleaseDate());
         }
      }
      return movies;
   }

}
