package com.example.calenderproject.fragments.menu_container;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.R;
import com.example.calenderproject.models.Channel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyChannelsFragment extends Fragment {
    private ArrayList<Channel> data = new ArrayList<>();
    private DatabaseReference ref;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_my_channels, container, false );
        final ImageButton buttonGoCreate = view.findViewById( R.id.buttonToCreateChannel );
        buttonGoCreate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoToFragment( "CreateChannel" );
                buttonGoCreate.setClickable( false );
            }
        } );
        RecyclerView ChannelView = view.findViewById( R.id.ChannelView );

        MyAdapter adapter = new MyAdapter( getData() );

        ChannelView.setAdapter( adapter );

        RecyclerView.LayoutManager manager = new LinearLayoutManager( getActivity() );

        ChannelView.setHasFixedSize( true );
        ChannelView.setNestedScrollingEnabled( true );

        ChannelView.setLayoutManager( manager );
        return view;
    }

    void GoToFragment(String Fragment) {
        InterfaceFragment interfaceFragment = new InterfaceFragment();
        CreateChannelFragment createChannelFragment = new CreateChannelFragment();
        MyChannelsFragment myChannelsFragment = new MyChannelsFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        switch (Fragment) {
            case "CreateChannel":
                fragmentTransaction.add( R.id.my_channel_container, createChannelFragment );
                break;
            case "MyChannels":
                //fragmentTransaction.show( interfaceFragment );
                fragmentTransaction.remove( createChannelFragment );
                break;
        }
        fragmentTransaction.commit();
    }

    @SuppressLint("ShowToast")
    private ArrayList<Channel> getData() {
        ref = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() );
        ref.addValueEventListener( new ValueEventListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String, HashMap<String, String>> AllMap = (HashMap) dataSnapshot.getValue();

                if (AllMap != null) {
                    HashMap<String, String> map = AllMap.get( "groups" );
                    for (String key : map.keySet()) {

                        String groupName = map.get( (String) key );
                        data.add( new Channel( (String) key ) );
                    }

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );
        return data;
    }
}
