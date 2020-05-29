package com.example.calenderproject.UI.menu_container;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.EventAdapter;
import com.example.calenderproject.R;
import com.example.calenderproject.objects.Event;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TreeMap;

public class AccountFragment extends CyaneaFragment {
    private TextView nameView;
    private RecyclerView EventView;
    private TreeMap<String, String> MapOfEvents = new TreeMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_account, container, false );
        nameView = view.findViewById( R.id.AccountNameView );
        EventView = view.findViewById( R.id.AccountEventView );
        TreeMap<String, String> test = new TreeMap<>();
        try {
            Log.e( "test", "yes" );
            FileInputStream SmallDude = getContext().openFileInput( "events.txt" );
            InputStreamReader reader = new InputStreamReader( SmallDude );
            BufferedReader redr = new BufferedReader( reader );
            StringBuffer lover = new StringBuffer();
            String leaver = new String();
            //  while(redr.readLine()!=null)
            // {lover.append( redr.readLine() );}
            //Log.e( "xx", redr.readLine() );
            String[] harassment = redr.readLine().split( "@@@@@@" );
            int lena = harassment.length;
            for (int j = 0; j < lena; j++) {
                if (!harassment[j].contains("1970-01-12 23:23" )) {
                    test.put( harassment[j], "." );
                }

            }
            for (String key : test.keySet()) {
                String ReadableTime;
                String RedactTime[] = key.split( ":" );
                if (RedactTime[0].length() == 1) {
                    int lena3 = RedactTime.length;
                    ReadableTime = "0";
                    for (int j = 0; j < lena3; j++) {
                        ReadableTime = ReadableTime + RedactTime[j];
                    }

                } else {
                    ReadableTime = key;
                }
                String value = test.get( key );
                MapOfEvents.put( ReadableTime, value );}
        } catch (IOException e) {
            Log.e( "testAccountRecycler","noooooo" );
            e.printStackTrace();
        }
        EventAdapter adapter = new EventAdapter( getData( MapOfEvents ) );
        EventView.setAdapter( adapter );
        RecyclerView.LayoutManager manager = new LinearLayoutManager( getContext() );

        EventView.setHasFixedSize( true );
        EventView.setNestedScrollingEnabled( true );

        EventView.setLayoutManager( manager );
        ImageButton SettingsButton = view.findViewById( R.id.ToSettingsButton );
        SettingsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = new SettingsFragment();
                getParentFragmentManager().beginTransaction().replace( R.id.menu_container, settingsFragment ).commit();
            }
        } );

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            FileInputStream SmallDude = getContext().openFileInput( "user.txt" );
            InputStreamReader reader = new InputStreamReader(SmallDude);
            BufferedReader redr = new BufferedReader( reader );
            nameView.setText( redr.readLine() );
            redr.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList getData(TreeMap Map) {
        ArrayList<Event> data = new ArrayList<>();
        for (Object key : Map.keySet()) {
            data.add( new Event( (String) key, (String) Map.get( key ) ) );
        }
        return data;
    }


}
