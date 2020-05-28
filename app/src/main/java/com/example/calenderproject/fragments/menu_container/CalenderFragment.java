package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.EventAdapter;
import com.example.calenderproject.R;
import com.example.calenderproject.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public  class CalenderFragment extends CyaneaFragment {
    private static final String TAG = "CalenderFragment";
    private DatabaseReference ref;
    private TreeMap<String, String> MapOfEvents = new TreeMap<>(  );
    private RecyclerView CalenderEventView;
    private DatabaseReference ref1;
    private DatabaseReference ref2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_calender, container, false );
        CalenderEventView = view.findViewById( R.id.CalenderEventView );









        CalendarView calendarView = view.findViewById( R.id.calendarView );
        final TextView testing = view.findViewById( R.id.testingtextView2 );
        calendarView.setOnDateChangeListener( (calendarView1, i, i1, i2) -> {
            final String DayOfEvent;
            if (i1>9 && i2>9) {
                 DayOfEvent = i + "-" + i1 + "-" + i2;
            }
            else if (i1<10 && i2>9) {
                 DayOfEvent = i + "-0" + i1 + "-" + i2;
            }
            else{
                 DayOfEvent = i + "-0" + i1 + "-0" + i2;
            }
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            ref = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() ).child( "events" );
            ref.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();
                    TreeMap<String,String> test = new TreeMap<>( );
                    for (String key : map.keySet()) {
                        HashMap<String, String> data = map.get( key );
                        for (String key2 : data.keySet()) {
                            if (data.get( key2 ).contains( DayOfEvent )) {
                                String[] time = data.get( key2 ).split( " " );
                                String HourAndMin = time[1];
                                //MapOfEvents.put( HourAndMin, key2 );
                                test.put( HourAndMin+"      "+key2,"." );
                                //Log.e( "error",key2);

                            }
                        }

                    }
                    for (String key:test.keySet()){
                        String ReadableTime;
                        String RedactTime[] = key.split(":");
                        if(RedactTime[0].length()==1){
                            int lena=RedactTime.length;
                            ReadableTime = "0";
                            for (int j = 0; j <lena ; j++) {
                                ReadableTime =ReadableTime +RedactTime[j];
                            }

                        }
                        else{
                             ReadableTime = key;
                        }
                        String value = test.get( key );
                        MapOfEvents.put(ReadableTime,value);
                    }
                    testing.setText( MapOfEvents.toString() );
                    EventAdapter adapter = new EventAdapter(getData( MapOfEvents ) );
                    CalenderEventView.setAdapter(adapter);
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());

                    CalenderEventView.setHasFixedSize(true);
                    CalenderEventView.setNestedScrollingEnabled(true);

                    CalenderEventView.setLayoutManager(manager);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            } );
        } );

        return view;
    }
    public ArrayList getData(TreeMap Map){
        ArrayList<Event> data =  new ArrayList<>( );
        for (Object key :Map.keySet()){
            data.add( new Event( (String) key, (String) Map.get( key ) ));
        }
        return data;
    }
}
