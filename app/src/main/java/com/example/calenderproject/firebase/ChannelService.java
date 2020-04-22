package com.example.calenderproject.firebase;

import com.example.calenderproject.models.Channel;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChannelService {
    public static void createNewChannel(String name, ArrayList ChannelUsers) {
        Channel channel = new Channel(ChannelUsers,name );

        FirebaseDatabase.getInstance()
                .getReference( "channels" )
                .push()
                .setValue( channel );
    }
}
