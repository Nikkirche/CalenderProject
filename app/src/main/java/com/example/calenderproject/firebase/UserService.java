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


    public static void createNewUser(String email, String name,String id/* ArrayList StartChannels*/) {

       // User user = new User( email, name, StartChannels );
       //  FirebaseAuth auth;
       ////  FirebaseDatabase ref= new FirebaseDatabase();
      //   FirebaseUser LocalUser  = auth.getInstance().getCurrentUser();

        HashMap<String, HashMap<String, String>> values = new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        data.put( "email", email );
        data.put( "name",name );
        values.put("id", data);
       //// ref= FirebaseDatabase.getInstance().getReference("Users").child(id);
      ////  ref.setValue(values);

        FirebaseDatabase.getInstance()
                .getReference( "users" ).child( id )
                .setValue( values );
    }
}