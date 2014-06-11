package com.mrt;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.movieproject.R;
import com.mrt.model.Theater;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SavedTheatersFragment extends Fragment
{
   private SharedPreferences mSavedValues = null;
   boolean mLoaded = false;

   private Theater mSelectedTheater = null;
   
   public SavedTheatersFragment()
   {

   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
         Bundle savedInstanceState)
   {
      View view =
            inflater.inflate(R.layout.fragment_saved_theater, container, false);

      Context context = getActivity();
      mSavedValues = context.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

      ArrayAdapter<Theater> adapter = new ArrayAdapter<Theater>(getActivity(),
            android.R.layout.simple_list_item_1, getSavedTheaters());

      OnItemClickListener listViewClickListener = new OnItemClickListener()
      {
         public void onItemClick(AdapterView<?> adapterView, View v, int position,
               long id)
         {
            mSelectedTheater =
                  (Theater) adapterView.getItemAtPosition(position);
            AlertDialog.Builder builder =
                  new AlertDialog.Builder(getActivity());
            builder.setMessage("Remove '" + mSelectedTheater.getName() + "' from saved theaters?");
            // Add the buttons
            builder.setPositiveButton(R.string.ok,
                  new DialogInterface.OnClickListener()
                  {
                     public void onClick(DialogInterface dialog, int id)
                     {
                        removeSelectedTheater();
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
      
      ListView theaterList =
            (ListView) view.findViewById(R.id.savedTheaterList);
      theaterList.setAdapter(adapter);
      theaterList.setOnItemClickListener(listViewClickListener);
      mLoaded = true;
      return view;
   }
   
   public void removeSelectedTheater()
   {
      SharedPreferences.Editor editor = mSavedValues.edit();
      editor.remove(mSelectedTheater.getId());
      editor.commit();
      updateSavedTheaterList();
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
               updateSavedTheaterList();
            }
         }
      }
   }

   @SuppressWarnings("unchecked")
   private void updateSavedTheaterList()
   {
      ListView savedTheaterList =
            (ListView) getView().findViewById(R.id.savedTheaterList);
      ArrayAdapter<Theater> adapter =
            (ArrayAdapter<Theater>) savedTheaterList.getAdapter();
      adapter.clear();
      adapter.addAll(getSavedTheaters());
      adapter.notifyDataSetChanged();
   }

   private List<Theater> getSavedTheaters()
   {
      List<Theater> savedTheaters = new ArrayList<Theater>();
      for (String id : mSavedValues.getAll().keySet())
      {
         if (!id.contains("movies"))
         {
        	 //update for zip code
            savedTheaters.add(new Theater(id, mSavedValues.getString(id, null), 00000));
         }
      }
      return savedTheaters;
   }
}
