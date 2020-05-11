package com.example.calenderproject.firebase;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

class UserService {
    private static final String TAG = "UserService";


    public static void createNewUser(String email, String name,String id) {

        HashMap<String, HashMap<String, String>> values = new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        data.put( "email", email );
        data.put( "name",name );
        values.put("id", data);

        FirebaseDatabase.getInstance()
                .getReference( "users" ).child( id )
                .setValue( values );
    }
}