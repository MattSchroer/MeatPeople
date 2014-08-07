package com.meatpeople.meatpeople;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.meatpeople.meatpeople.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainPage extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new EventListFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class EventListFragment extends Fragment {

        public EventListFragment() {
        }

        String[] eventArray = {
                "Restaurant - Peter, Greg, Ben - 6:00",
                "Bar - Dan, Matt, Ben - 9:00",
                "Cafe - Peter, Dan, Matt - 5:00",
                "Restaurant - Greg, Ben, Matt - 7:00",
                "Bar - Emily, Sarah, Joe - 9:30"
        };

        List<String> eventList = new ArrayList<String>(Arrays.asList(eventArray));

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            ArrayAdapter<String> eventAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item_events,R.id.list_item_events_textview,eventList);
            View rootView = inflater.inflate(R.layout.fragment_main_page, container, false);

            ListView listView = (ListView) rootView.findViewById(R.id.listview_events);
            listView.setAdapter(eventAdapter);
            return rootView;
        }
    }
}