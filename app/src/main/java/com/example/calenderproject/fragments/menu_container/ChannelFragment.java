package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChannelFragment extends Fragment {
    TextView NameView;
    String ChannelName,SubberName;
    private static DatabaseReference refUser;
    private static FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ChannelName = bundle.getString( "ChannelName" );

            NameView.setText( ChannelName );
        } else {
            NameView.setText( "Error" );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_channel, container, false );
        NameView = view.findViewById( R.id.ChannelFragmentNameView );


        ImageButton UnsubscribeButton = view.findViewById( R.id.buttonUnsubscribe );
        UnsubscribeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refUser = FirebaseDatabase.getInstance().getReference( "users" ).child( GroupUser.getUid() );

                refUser.addValueEventListener( new ValueEventListener() {
                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                       HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();

                                                       HashMap<String, String> data1 = new HashMap<>();
                                                       HashMap<String, String> data2 = new HashMap<>();
                                                       data1 = map.get( "groups" );


                                                       for (String key : data1.keySet()) {

                                                           String TrueKey = data1.get( key );
                                                           if (TrueKey != ChannelName) {
                                                               data2.put( TrueKey, TrueKey );
                                                           }
                                                       }
                                                       map.put( "groups", data2 );
                                                       SubberName=map.get("id").get("name");


                                                       refUser.setValue( map );

                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                   }


                                               }
                );
                refUser = FirebaseDatabase.getInstance().getReference( "Channels" ).child(ChannelName  );
                refUser.addValueEventListener( new ValueEventListener() {
                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                       HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();

                                                       HashMap<String, String> data1 = new HashMap<>();
                                                       HashMap<String, String> data2 = new HashMap<>();
                                                       data1 = map.get( "subscribers" );


                                                       for (String key : data1.keySet()) {

                                                           String Subber = data1.get( key );
                                                           if (Subber != SubberName) {
                                                               data2.put( Subber, Subber );
                                                           }
                                                       }
                                                       map.put( "groups", data2 );


                                                       refUser.setValue( map );

                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                   }


                                               }
                );


            }
        });
        return view;


    }
}