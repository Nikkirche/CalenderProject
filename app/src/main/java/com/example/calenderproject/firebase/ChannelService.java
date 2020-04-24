package com.example.calenderproject.firebase;

import androidx.annotation.NonNull;

import com.example.calenderproject.models.Channel;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChannelService {
    private static DatabaseReference refUser;
    private static FirebaseDatabase refChannel;
    public static void createNewChannel(final String name, HashMap <String,HashMap <String,String>> ChannelUsers, String CurrentUser){
        Channel channel = new Channel(ChannelUsers,name );
        refUser = FirebaseDatabase.getInstance().getReference( "users" ).child( CurrentUser );

        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                map.get("id").put(name,name);


                refUser.setValue(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );

        HashMap<String, HashMap <String, String>> values = new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        data.put( "name",name );

        values.put("id", data);
        FirebaseDatabase.getInstance()
                .getReference( "Channels" ).child( name )
                .setValue( values );
    }
}
