package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import java.util.HashMap;

public class AccountFragment extends CyaneaFragment {
    private DatabaseReference ref;
    private TextView nameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_account, container, false );
        nameView = view.findViewById( R.id.AccountNameView );
        ImageButton SettingsButton = view.findViewById( R.id.ToSettingsButton );
        SettingsButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = new SettingsFragment();
                getParentFragmentManager().beginTransaction().replace( R.id.menu_container, settingsFragment ).commit();
            }
        } );
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
                HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();
                nameView.setText( map.get( "id" ).get( "name" ) );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                nameView.setText( "Error!!!" );
            }
        } );
    }
}
