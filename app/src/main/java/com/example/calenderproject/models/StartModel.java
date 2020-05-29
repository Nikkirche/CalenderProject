package com.example.calenderproject.models;

import androidx.annotation.NonNull;

import com.example.calenderproject.objects.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class StartModel {
    private static final String TAG = "StartModel";
    private static DatabaseReference ref;
    private static HashMap<String, HashMap<String, HashMap<String, String>>> values1;

    public Task<AuthResult> signInEmailAndPassword(final String email, String password, final String name) {
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

    public void writeUserData(FileOutputStream Stream, String name) throws IOException {
        Stream.flush();
        Stream.write( name.getBytes() );
        Stream.close();
    }

    public Task<AuthResult> signIn(final String email, String password) {
        return FirebaseAuth.getInstance()
                .signInWithEmailAndPassword( email, password );
    }

    public boolean StatusOfUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser() != null;
    }

    private void createNewUser(String email, String name, String id) {
        HashMap<String, HashMap<String, String>> val;
        HashMap<String, HashMap<String, String>> values = new HashMap<>();
        HashMap<String, String> data = new HashMap<>();
        HashMap<String, String> data2 = new HashMap<>();

        data.put( "email", email );
        data.put( "name", name );
        data2.put( "zerogroup", "zerogroup" );

        values.put( "id", data );
        values.put( "Subchannels", data2 );

        FirebaseDatabase.getInstance()
                .getReference( "users" ).child( id )
                .setValue( values );
    }

    public void  GetSignData(FileOutputStream Stream ) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "users" ).
                child( firebaseUser.getUid() ).child( "id" );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> izi = (HashMap) dataSnapshot.getValue();
                String name = izi.get( "name" );
                try {
                    writeUserData(Stream,name  );
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );
    }
}
