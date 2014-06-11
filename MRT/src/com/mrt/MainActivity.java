package com.mrt;

import com.movieproject.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends Activity
{
   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      // Notice that setContentView() is not used, because we use the root
      // android.R.id.content as the container for each fragment

      // setup action bar for tabs
      ActionBar actionBar = getActionBar();
      actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
      actionBar.setDisplayShowTitleEnabled(false);
      actionBar.setTitle("Movie Release Tracker");
      Tab tab = actionBar.newTab()
                         .setText(R.string.movie_tab_title)
                         .setTabListener(new TabListener<MoviesFragment>(
                                 this, "movies", MoviesFragment.class));
      actionBar.addTab(tab);

      tab = actionBar.newTab()
                     .setText(R.string.theater_tab_title)
                     .setTabListener(new TabListener<TheatersFragment>(
                             this, "theaters", TheatersFragment.class));
      actionBar.addTab(tab);
      
      tab = actionBar.newTab()
            .setText(R.string.saved_theater_tab_title)
            .setTabListener(new TabListener<SavedTheatersFragment>(
                    this, "savedTheaters", SavedTheatersFragment.class));
      actionBar.addTab(tab);

   }

   @Override
   public boolean onCreateOptionsMenu(Menu aMenu)
   {
      // Inflate the menu items for use in the action bar
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.main, aMenu);
      return super.onCreateOptionsMenu(aMenu);

   }

   public static class TabListener<T extends Fragment> implements
         ActionBar.TabListener
   {
      private Fragment mFragment;
      private final Activity mActivity;
      private final String mTag;
      private final Class<T> mClass;

      /**
       * Constructor used each time a new tab is created.
       * 
       * @param activity
       *           The host Activity, used to instantiate the fragment
       * @param tag
       *           The identifier tag for the fragment
       * @param clz
       *           The fragment's Class, used to instantiate the fragment
       */
      public TabListener(Activity activity, String tag, Class<T> clz)
      {
         mActivity = activity;
         mTag = tag;
         mClass = clz;
      }

      /* The following are each of the ActionBar.TabListener callbacks */

      public void onTabSelected(Tab tab, FragmentTransaction ft)
      {
         // Check if the fragment is already initialized
         if (mFragment == null)
         {
            // If not, instantiate and add it to the activity
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            ft.add(android.R.id.content, mFragment, mTag);
         }
         else
         {
            // If it exists, simply attach it in order to show it
            ft.attach(mFragment);
         }
      }

      public void onTabUnselected(Tab tab, FragmentTransaction ft)
      {
         if (mFragment != null)
         {
            // Detach the fragment, because another one is being attached
            ft.detach(mFragment);
         }
      }

      public void onTabReselected(Tab tab, FragmentTransaction ft)
      {
         // User selected the already selected tab. Usually do nothing.
      }
   }

}
