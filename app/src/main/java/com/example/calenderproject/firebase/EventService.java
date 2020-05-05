package com.example.calenderproject.firebase;

import android.icu.util.LocaleData;

import androidx.annotation.NonNull;

import com.example.calenderproject.models.Channel;
import com.example.calenderproject.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class EventService {
    private static DatabaseReference refUser;
    private static DatabaseReference refChannel;
    private static HashMap<String, HashMap<String, Integer>> UserChannelEvent;
    private static HashMap<String, HashMap<String, HashMap<String, Integer>>> BigMap;
    private static HashMap<String, String> SubscriberChannelEvent;


    public static void createNewEvent(String ChannelName, final Integer time, final String text) {
        final FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        Event event = new Event( text, time );
        /*refUser = FirebaseDatabase.getInstance().getReference( "users" ).child( CurrentUser.getUid() );
        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, Integer>> map = (HashMap) dataSnapshot.getValue();
                if (map.get( "events" ) == null) {
                    HashMap<String, Integer> data = new HashMap<>();
                    data.put( text, time );
                    map.put( "events", data );
                } else {
                    map.get( "events" ).put( text, time );
                }
                refUser.setValue( map );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        } );*/
        refChannel = FirebaseDatabase.getInstance().getReference( "Channels" ).child( ChannelName );
        refChannel.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, HashMap<String, Integer>>> map1 = (HashMap) dataSnapshot.getValue();
                String EventId = CurrentUser.getDisplayName() + Calendar.getInstance().getTime().toString();
                if (map1 != null) {
                    if (map1.get( "events" ) == null) {
                        HashMap<String, HashMap<String, Integer>> data1 = new HashMap<>();
                        HashMap<String, Integer> mini = new HashMap<>();
                        mini.put( text, time );
                        data1.put( EventId, mini );
                        map1.put( "events", data1 );
                    } else {
                        HashMap<String, Integer> mini = new HashMap<>();
                        mini.put( text, time );
                        map1.get( "events" ).put( EventId, mini );
                    }
                }
                refChannel.setValue( map1 );
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        } );

        refUser = FirebaseDatabase.getInstance().getReference( "Channels" ).child( ChannelName );
        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, HashMap<String, Integer>>> map = (HashMap) dataSnapshot.getValue();
                if (map != null) {
                    UserChannelEvent = map.get( "events" );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );
        refUser = FirebaseDatabase.getInstance().getReference( "Channels" ).child( ChannelName );
        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();
                if (map != null) {
                    SubscriberChannelEvent = map.get( "subscribers" );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );
        if (SubscriberChannelEvent != null) {
            for (String key : SubscriberChannelEvent.keySet()) {
                BigMap.put( key, UserChannelEvent );

            }
        }



        refUser = FirebaseDatabase.getInstance().getReference( "users" );
        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,HashMap<String, HashMap<String, HashMap<String, Integer>>>> map = (HashMap) dataSnapshot.getValue();

                    for (String key1 : map.keySet()) {
                        if (map.get(key1).get("events") != null) {
                        if (BigMap != null) {
                            for (String key2 : BigMap.keySet()) {
                                if (key1.equals(key2))
                                {
                                    for (String key : BigMap.keySet())
                                        map.get(key1).get("events").put(key,UserChannelEvent.get(key));
                                }

                            }
                        }

                    }
                else{
                            map.get(key1).put("events",UserChannelEvent);

                        }


                }
                refUser.setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        } );

        /*refUser = FirebaseDatabase.getInstance().getReference( "users" );
        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String,HashMap<String,String>>> miniMap1 = (HashMap) dataSnapshot.getValue();
            HashMap  < String, HashMap<String, HashMap<String,HashMap<String,Integer>>>> miniMap2 = (HashMap) dataSnapshot.getValue();

                if (miniMap1 != null) {
                    for (String key1 : miniMap1.keySet()) {

                        if (BigMap != null) {
                            for (String key2 : BigMap.keySet()) {

                                if(miniMap1.get(key1).get(   "id" ).get( "name" ).equals( key2 )){
                                    if (){}
                                    else{
                                        HashMap<String,HashMap<String, Integer>> mop=BigMap.get(key2);
                                        miniMap2.get( key1 ).get( "events" ).put(mop);
                                    }
                                }

                            }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        } );
        */




        /*HashMap<String, HashMap <String, String>> values = new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        data.put( "name",name );
        HashMap<String, String> dataSubs = new HashMap<>();
        dataSubs.put( nameOfCurrentUser,nameOfCurrentUser );

        values.put("id", data);
        values.put("subscribers", dataSubs);
        FirebaseDatabase.getInstance()
                .getReference( "Channels" ).child( name )
                .setValue( values );

         */
    }
}
