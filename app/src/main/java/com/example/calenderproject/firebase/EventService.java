package com.example.calenderproject.firebase;

import com.example.calenderproject.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class EventService {
    private static DatabaseReference refUser;
    private static DatabaseReference refChannel;
    private static DatabaseReference refToUser;
    private static HashMap<String, HashMap<String, String>> UserChannelEvent;
    private static HashMap<String, String> SubscriberChannelEvent;
    private static HashMap<String, HashMap<String, HashMap<String, String>>> mapp1;
    private static HashMap<String,HashMap<String, HashMap<String, HashMap<String, String>>>> mapp;
    private static HashMap<String,HashMap<String, HashMap<String, HashMap<String, String>>>> mapp2;

    public static void  SetEvent( HashMap<String, HashMap<String, String>> UserChannelEventZ,
             HashMap<String, String> SubscriberChannelEventZ,
             HashMap<String, HashMap<String, HashMap<String, String>>> mapp1Z,
             HashMap<String,HashMap<String, HashMap<String, HashMap<String, String>>>> mappZ,
             HashMap<String,HashMap<String, HashMap<String, HashMap<String, String>>>> mapp2Z
    )
    {
        UserChannelEvent=UserChannelEventZ;
        SubscriberChannelEvent=SubscriberChannelEventZ;
        mapp1=mapp1Z;
        mapp=mappZ;
        mapp2=mapp2Z;

    }
    public static void createNewEvent(String ChannelName, final String time, final String text) {
        final FirebaseUser CurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        Event event = new Event( text, time );

        refChannel = FirebaseDatabase.getInstance().getReference( "Channels" ).child( ChannelName );

                String EventId = CurrentUser.getDisplayName() + Calendar.getInstance().getTime().toString();
                if (mapp1 != null) {
                    if (mapp1.get( "events" ) == null) {
                        HashMap<String, HashMap<String, String>> data1 = new HashMap<>();
                        HashMap<String, String> mini = new HashMap<>();
                        mini.put( text, time );
                        data1.put( EventId, mini );
                        mapp1.put( "events", data1 );
                    } else {
                        HashMap<String, String> mini = new HashMap<>();
                        mini.put( text, time );
                        mapp1.get( "events" ).put( EventId, mini );
                    }
                }
                UserChannelEvent = mapp1.get("events");
                refChannel.setValue( mapp1 );









        try {
            Thread.sleep(1000);
            refToUser = FirebaseDatabase.getInstance().getReference( "users" );

                        for (String key1 : mapp.keySet()) {
                        if (mapp.get(key1).get("events") != null) {
                            if (SubscriberChannelEvent != null) {
                                for (String key2 : SubscriberChannelEvent.keySet()) {
                                    if (key1.equals(key2))
                                    {
                                        for (String key : UserChannelEvent.keySet())
                                            mapp2.get(key1).get("events").put(key,UserChannelEvent.get(key));
                                    }

                                }
                            }

                        }
                        else{
                            mapp2.get(key1).put("events",UserChannelEvent);

                        }


                    }
                    refToUser.setValue(mapp2);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }




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
