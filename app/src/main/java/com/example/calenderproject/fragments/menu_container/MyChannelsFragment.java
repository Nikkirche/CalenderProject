package com.example.calenderproject.fragments.menu_container;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.R;
import com.example.calenderproject.models.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MyChannelsFragment extends Fragment {
    private DatabaseReference ref;
    private RecyclerView channelView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseRecyclerAdapter adapter;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

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
        channelView = view.findViewById( R.id.ChannelView );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        channelView.setLayoutManager( linearLayoutManager );
        channelView.setHasFixedSize( true );
        fetch();
        return view;
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {
        TextView ChannelNameTextView;

        public ChannelViewHolder(@NonNull View itemView) {
            super( itemView );

            ChannelNameTextView = itemView.findViewById( R.id.ChannelNameTextView );
        }

        public void bind(Channel channel) {
            ChannelNameTextView.setText( channel.name );
        }
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() ).child( "groups" );
        FirebaseRecyclerOptions<Channel> options =
                new FirebaseRecyclerOptions.Builder<Channel>()
                        .setQuery(query, new SnapshotParser<Channel>() {
                            @NonNull
                            @Override
                            public Channel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Channel(snapshot.getValue().toString());
                            }
                        })
                        .build();


                                adapter = new FirebaseRecyclerAdapter<Channel, ChannelViewHolder>( options ) {
                                    @Override
                                    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                                        View view = LayoutInflater.from( parent.getContext() )
                                                .inflate( R.layout.item_channel, parent, false );

                                        return new ChannelViewHolder( view );
                                    }


                                    @Override
                                    protected void onBindViewHolder(ChannelViewHolder holder, final int position, final Channel channel) {
                                        final TextView NameView = holder.ChannelNameTextView;
                                        NameView.setText( channel.getName() );
                                        holder.itemView.setOnClickListener( new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                ChannelFragment channelFragment = new ChannelFragment();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("ChannelName",channel.getName() );
                                                channelFragment.setArguments(bundle);
                                                getChildFragmentManager().beginTransaction().add( R.id.my_channel_container,channelFragment ).commit();

                                            }
                                        } );
                                    }

                                };
        channelView.setAdapter( adapter );
    }

    void GoToFragment(String Fragment) {
        InterfaceFragment interfaceFragment = new InterfaceFragment();
        CreateChannelFragment createChannelFragment = new CreateChannelFragment();
        MyChannelsFragment myChannelsFragment = new MyChannelsFragment();
        ChannelFragment channelFragment = new ChannelFragment();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        switch (Fragment) {
            case "CreateChannel":
                fragmentTransaction.add( R.id.my_channel_container, createChannelFragment );
                break;
            case "rCreateChannel":
                fragmentTransaction.remove( createChannelFragment );
                break;
            case "rChannel":
              fragmentTransaction.remove( channelFragment );
              break;
            case "Channel":
                fragmentTransaction.add( R.id.my_channel_container,channelFragment);
                break;
            default:
                fragmentTransaction.add( R.id.my_channel_container, createChannelFragment );
        }
        fragmentTransaction.commit();
    }


}
