package com.example.calenderproject.firebase;

import com.example.calenderproject.models.User;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserService {
    private static final String TAG = "UserService";

    public static void createNewUser(String email, String name, ArrayList StartChannels) {
        User user = new User( email, name, StartChannels );

        FirebaseDatabase.getInstance()
                .getReference( "users" )
                .push()
                .setValue( user );
    }
}