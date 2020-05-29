package com.example.calenderproject.UI.menu_container;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.EventAdapter;
import com.example.calenderproject.R;
import com.example.calenderproject.objects.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class CalenderFragment extends CyaneaFragment {
    private static final String TAG = "CalenderFragment";
    private DatabaseReference ref;
    private TreeMap<String, String> MapOfEvents = new TreeMap<>();
    private RecyclerView CalenderEventView;
    private DatabaseReference ref1;
    private DatabaseReference ref2;
    HashMap<String, HashMap<String, String>> map;

    @Override
    public void onStart() {
        super.onStart();
        Log.e( "test"," gavno" );
        if (isOnline()) {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            ref = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() ).child( "events" );
            ref.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    map = (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                    Log.e( "test",map.toString() );

                }

                @SuppressLint("CheckResult")
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //Toasty.error( getContext(),R.string.no_internet  );
                }
            } );
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL( "https://myitschool.ru" );
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout( 3000 );
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                }
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_calender, container, false );
        setPolicy();
        Button updateButton = view.findViewById( R.id.buttonUpdateEvents );
        updateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e( "test"," gavno" );
                String infadata = new String();
                for (String yep : map.keySet()) {
                    HashMap<String, String> minimap = map.get( yep );
                    for (String yep1 : minimap.keySet()) {
                        infadata = infadata + minimap.get( yep1 ) + "      " + yep1 + "@@@@@@";

                    }

                }
                Log.e( "xx", infadata );
                try {
                    FileOutputStream BigDude = getContext().openFileOutput( "example.txt", Context.MODE_PRIVATE );
                    BigDude.flush();
                    BigDude.write( infadata.getBytes() );
                    BigDude.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } );

        CalenderEventView = view.findViewById( R.id.CalenderEventView );
        CalendarView calendarView = view.findViewById( R.id.calendarView );
        final TextView testing = view.findViewById( R.id.testingtextView2 );



        calendarView.setOnDateChangeListener( (calendarView1, i, i1, i2) -> {
            MapOfEvents.clear();
            final String DayOfEvent;
            if (i1 > 9 && i2 > 9) {
                DayOfEvent = i + "-" + i1 + "-" + i2;
            } else if (i1 < 10 && i2 > 9) {
                DayOfEvent = i + "-0" + i1 + "-" + i2;
            } else {
                DayOfEvent = i + "-0" + i1 + "-0" + i2;
            }

            if (isOnline()) {
                TreeMap<String, String> test = new TreeMap<>();
                for (String key : map.keySet()) {
                    HashMap<String, String> data = map.get( key );
                    for (String key2 : data.keySet()) {
                        if (data.get( key2 ).contains( DayOfEvent )) {
                            String[] time = data.get( key2 ).split( " " );
                            String HourAndMin = time[1];
                            //MapOfEvents.put( HourAndMin, key2 );
                            test.put( HourAndMin + "      " + key2, "." );
                            //Log.e( "error",key2);

                        }
                    }

                }
                for (String key : test.keySet()) {
                    String ReadableTime;
                    String RedactTime[] = key.split( ":" );
                    if (RedactTime[0].length() == 1) {
                        int lena = RedactTime.length;
                        ReadableTime = "0";
                        for (int j = 0; j < lena; j++) {
                            ReadableTime = ReadableTime + RedactTime[j];
                        }

                    } else {
                        ReadableTime = key;
                    }
                    String value = test.get( key );
                    MapOfEvents.put( ReadableTime, value );
                }
                EventAdapter adapter = new EventAdapter( getData( MapOfEvents ) );
                CalenderEventView.setAdapter( adapter );
                RecyclerView.LayoutManager manager = new LinearLayoutManager( getContext() );

                CalenderEventView.setHasFixedSize( true );
                CalenderEventView.setNestedScrollingEnabled( true );

                CalenderEventView.setLayoutManager( manager );
                //testing.setText( MapOfEvents.toString() );

            } else {
                Log.e( "test", "no" );
                TreeMap<String, String> test = new TreeMap<>();

                try {
                    FileInputStream SmallDude = getContext().openFileInput( "example.txt" );
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
                        if (harassment[j].contains( DayOfEvent )) {
                            String[] time = harassment[j].split( " " );
                            int lena1 = time.length;
                            String HourAndMin = new String();
                            for (int k = 1; k < lena1; k++) {
                                HourAndMin = HourAndMin + " " + time[k];
                            }


                            test.put( HourAndMin, "." );
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
                    e.printStackTrace();
                }
                EventAdapter adapter = new EventAdapter( getData( MapOfEvents ) );
                CalenderEventView.setAdapter( adapter );
                RecyclerView.LayoutManager manager = new LinearLayoutManager( getContext() );

                CalenderEventView.setHasFixedSize( true );
                CalenderEventView.setNestedScrollingEnabled( true );

                CalenderEventView.setLayoutManager( manager );
            }
        } );


        return view;
    }

    private void setPolicy() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy( policy );
    }

    public ArrayList getData(TreeMap Map) {
        ArrayList<Event> data = new ArrayList<>();
        for (Object key : Map.keySet()) {
            data.add( new Event( (String) key, (String) Map.get( key ) ) );
        }
        return data;
    }
}
