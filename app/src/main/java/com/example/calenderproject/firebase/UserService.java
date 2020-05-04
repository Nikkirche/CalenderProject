package com.example.calenderproject.firebase;

import com.example.calenderproject.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class UserService {
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