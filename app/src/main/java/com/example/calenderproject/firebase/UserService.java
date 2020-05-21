package com.example.calenderproject.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

class UserService {
    private static final String TAG = "UserService";
    private static DatabaseReference ref;
    private static HashMap<String,HashMap<String, HashMap<String, String>>> values1;

    public static void createNewUser(String email, String name,String id) {
        HashMap<String, HashMap<String, String>> val;
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





    }





}