package com.example.calenderproject.firebase;

import com.example.calenderproject.models.User;
import com.google.firebase.database.FirebaseDatabase;

public class UserService {
    public static void createNewUser(String email, String name) {
        User user = new User(email, name);

        FirebaseDatabase.getInstance()
                .getReference("users")
                .push()
                .setValue(user);
    }
}