package com.example.calenderproject.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

class UserService {
    private static final String TAG = "UserService";
    private static DatabaseReference ref;

    public static void createNewUser(String email, String name,String id) {

        HashMap<String, HashMap<String, String>> values = new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, String> data2 = new HashMap<>();
        data.put( "email", email );
        data.put( "name",name );
        data2.put( "zerogroup","zerogroup" );

        values.put("id", data);
        values.put("Subchannels",data2);

        FirebaseDatabase.getInstance()
                .getReference( "users" ).child( id )
                .setValue( values );

        ref = FirebaseDatabase.getInstance().getReference( "users" ).child( id );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> data1 = new HashMap<>();
                HashMap<String,HashMap<String, HashMap<String, String>>> values1 = (HashMap)dataSnapshot.getValue();
                HashMap<String,String> nothing = new HashMap<>(  );
                nothing.put("Pashal Egg", "1970-01-12 23:23" );
                data1.put("PashalEgg69",nothing);
                values1.put("events",data1);
                ref.setValue(values1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }
}