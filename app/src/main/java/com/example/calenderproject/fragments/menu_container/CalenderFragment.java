package com.example.calenderproject.fragments.menu_container;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.AuthService;
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

import java.util.HashMap;

public class CalenderFragment extends Fragment {
    private static final String TAG = "CalenderFragment";
    private DatabaseReference ref;
    private TextView nameView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_calender, container, false );
        nameView = view.findViewById( R.id.textName );


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                nameView.setText( map.get("id").get( "name" ));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                nameView.setText( "shit" );
            }
        } );
    }

    ///  ValueEventListener nameListener = new ValueEventListener() {
    ///      @Override
    //       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
    //           User user = dataSnapshot.getValue(User.class);
    //           nameView.setText(user.name);
    //       }
}
