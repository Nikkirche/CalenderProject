package com.example.calenderproject.fragments;

import com.example.calenderproject.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class StartModel {
    private static final String TAG = "StartModel";
    private static DatabaseReference ref;
    private static HashMap<String,HashMap<String, HashMap<String, String>>> values1;
    public  Task<AuthResult> signInEmailAndPassword(final String email, String password, final String name) {
        return FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword( email, password )
                .addOnSuccessListener( new OnSuccessListener<AuthResult>() {
                    @Override

                    public void onSuccess(AuthResult authResult) {
                        final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        createNewUser( email, name, id );
                        User user = new User( id, name, email );
                    }
                } );
    }

    public  Task<AuthResult> signIn(final String email, String password) {
        return FirebaseAuth.getInstance()
                .signInWithEmailAndPassword( email, password );
    }

    public  boolean StatusOfUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser() != null;
    }
    private void createNewUser(String email, String name, String id) {
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
