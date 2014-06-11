package com.mrt;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.movieproject.R;
import com.mrt.matthew.OnConnectDataProcessor;
import com.mrt.matthew.OnConnectJSONDao;
import com.mrt.model.Movie;
import com.mrt.model.Theater;

public class TheatersFragment extends Fragment
{
   private List<Theater> mTheaters;
   private ArrayAdapter<Theater> mAdapter;
   private Theater mSelectedTheater = null;
   private SharedPreferences mSavedValues = null;
   
   private OnConnectDataProcessor mDataProcessor;

   public TheatersFragment()
   {
      mTheaters = new ArrayList<Theater>();
      
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
         Bundle savedInstanceState)
   {
      View view = inflater.inflate(R.layout.fragment_theater, container, false);
      mDataProcessor = new OnConnectDataProcessor(new OnConnectJSONDao(view.getContext()), true);
      Context context = getActivity();
      mSavedValues = context.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE);
      
      // Create on click handler
      Button button = (Button) view.findViewById(R.id.theaterSearchButton);
      button.setOnClickListener(new OnClickListener()
      {
         @Override
         public void onClick(View v)
         {
            EditText editText =
                  (EditText) getView().findViewById(R.id.theaterSearchText);
            populateTheatersList(editText.getText().toString());
            InputMethodManager mgr =
                  (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
         }
      });

      OnItemClickListener listViewClickListener = new OnItemClickListener()
      {
         public void onItemClick(AdapterView<?> adapterView, View v, int position,
               long id)
         {
            mSelectedTheater =
                  (Theater) adapterView.getItemAtPosition(position);
            AlertDialog.Builder builder =
                  new AlertDialog.Builder(getActivity());
            builder.setMessage("Add '" + mSelectedTheater.getName() + "' to saved theaters?");
            // Add the buttons
            builder.setPositiveButton(R.string.ok,
                  new DialogInterface.OnClickListener()
                  {
                     public void onClick(DialogInterface dialog, int id)
                     {
                        addSelectedTheaterToMemory();
                     }
                  });
            builder.setNegativeButton(R.string.cancel,
                  new DialogInterface.OnClickListener()
                  {
                     public void onClick(DialogInterface dialog, int id)
                     {
                        // Do nothing
                     }
                  });

            // Create the AlertDialog
            AlertDialog dialog = builder.create();

            dialog.show();

         }
      };

      ArrayAdapter<Theater> adapter = new ArrayAdapter<Theater>(getActivity(),
            android.R.layout.simple_list_item_1, mTheaters);

      ListView theaterList = (ListView) view.findViewById(R.id.theaterList);
      theaterList.setAdapter(adapter);
      theaterList.setOnItemClickListener(listViewClickListener);
      mAdapter = adapter;

      return view;
   }

   private void populateTheatersList(String aZip)
   {
      mAdapter.clear();
      // Call Matthew's code for theater list
      try
      {
         mTheaters =  mDataProcessor.findTheaterByZip(Integer.parseInt(aZip), 10);
      }
      catch (NumberFormatException e)
      {
         e.printStackTrace();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      mAdapter.addAll(mTheaters);
      mAdapter.notifyDataSetChanged();
   }

   private void addSelectedTheaterToMemory()
   {
      SharedPreferences.Editor editor = mSavedValues.edit();
      
      Set<String> stringSetValues = new LinkedHashSet<String>();
      for(Movie movie : mSelectedTheater.getAllMovies())
      {
    	  //errors on cinemark frisco square and xd theatre in 75024 and 75035 zip codes
         stringSetValues.add(movie.getMovieId() + "," + movie.getMovieName() + "," + movie.getReleaseDate().toString());
      }
      editor.putString(mSelectedTheater.getId(), mSelectedTheater.getName());
      editor.putStringSet(mSelectedTheater.getId()+"movies", stringSetValues);
      editor.commit();
   }
   
}
