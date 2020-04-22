package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.UserService;
import com.example.calenderproject.models.User;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CalenderFragment extends Fragment {
    private static final String TAG = "CalenderFragment";
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_calender, container, false );
        final TextView name = view.findViewById( R.id.textName );
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser ServerUser = firebaseAuth.getCurrentUser();
        DatabaseReference DataLoading = null;
        if (ServerUser != null) {
            DataLoading = FirebaseDatabase.getInstance().getReference( "users" ).child( ServerUser.getUid());
            DataLoading.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String  LocalUser = dataSnapshot.getValue( String.class );
                    if (LocalUser != null) {
                        name.setText( LocalUser );
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w( TAG, "Failed to read value.", databaseError.toException() );
                }
            } );
        }else{
            name.setText( "Gavno");

        }

        return view;
    }

}
