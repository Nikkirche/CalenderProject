package com.example.calenderproject.firebase;

import androidx.annotation.NonNull;

import com.example.calenderproject.models.Channel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChannelService {
    private static DatabaseReference refUser;
    private static FirebaseDatabase refChannel;
    public static void createNewChannel(final String name, HashMap <String,HashMap <String,String>> ChannelUsers, String CurrentUser,String nameOfCurrentUser ){
        Channel channel = new Channel(ChannelUsers,name );
        refUser = FirebaseDatabase.getInstance().getReference( "users" ).child( CurrentUser );

        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                if(map.get( "groups" )==null)
                {
                    HashMap<String, String> data = new HashMap<>();
                    data.put( name ,name );

                    map.put("groups", data);
                }
                else
                {map.get("groups").put(name,name);}


                refUser.setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );

        HashMap<String, HashMap <String, String>> values = new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        data.put( "name",name );
        HashMap<String, String> dataSubs = new HashMap<>();
        dataSubs.put( CurrentUser,CurrentUser );

        values.put("id", data);
        values.put("subscribers", dataSubs);
        FirebaseDatabase.getInstance()
                .getReference( "Channels" ).child( name )
                .setValue( values );
    }
}
