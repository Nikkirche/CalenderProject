package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.MorphAnimation;
import com.example.calenderproject.R;
import com.example.calenderproject.firebase.ChannelService;
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
import com.jaredrummler.cyanea.app.CyaneaFragment;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class MyChannelsFragment extends CyaneaFragment {
    private DatabaseReference ref;
    private RecyclerView channelView;
    private RecyclerView AdminChannelView;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager AdminLinearLayoutManager;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseRecyclerAdapter adapter;
    private  FirebaseRecyclerAdapter adapterAdmin;
    private HashMap<String,String>  values;
    private String nameOfCurrentUser;

    @Override
    public void onStart() {
        super.onStart();
        adapterAdmin.startListening();
        adapter.startListening();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();
                nameOfCurrentUser = map.get( "id" ).get( "name" );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        } );
    }

    @Override
    public void onStop() {
        super.onStop();
        adapterAdmin.stopListening();
        adapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_my_channels, container, false );
        final ImageButton buttonGoCreate = view.findViewById( R.id.buttonToCreateChannel );
        final  ImageButton buttonGoSearch = view.findViewById( R.id.buttonToSearch);
        ViewGroup CreateGroup = view.findViewById( R.id.create_channel_views );
        FrameLayout CreateLayout  = view.findViewById( R.id.createLayout );
        View CreateChannelContainer = view.findViewById( R.id.form_create_channel );
        channelView = view.findViewById( R.id.ChannelView );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        channelView.setLayoutManager( linearLayoutManager );
        channelView.setHasFixedSize( true );
        AdminChannelView = view.findViewById( R.id.AdminChannelView );
        AdminLinearLayoutManager = new LinearLayoutManager( this.getActivity() );
        AdminChannelView.setLayoutManager( AdminLinearLayoutManager );
        AdminChannelView.setHasFixedSize( true );
        fetch();
        fetchAdmin();
        buttonGoSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoToFragment( "Search" );
            }
        } );
        final MorphAnimation morphAnimationCreateChannel = new MorphAnimation(CreateChannelContainer , CreateLayout, CreateGroup );
        buttonGoCreate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GoToFragment( "CreateChannel" );
                if (!morphAnimationCreateChannel.isPressed()) {
                    buttonGoSearch.setVisibility( View.GONE);
                    channelView.setVisibility( View.GONE );
                    AdminChannelView.setVisibility( View.GONE );
                    morphAnimationCreateChannel.morphIntoForm("MATCH_PARENT");
                } else {
                    morphAnimationCreateChannel.morphIntoButton();
                    buttonGoSearch.setVisibility( View.VISIBLE);
                    channelView.setVisibility( View.VISIBLE );
                    AdminChannelView.setVisibility( View.VISIBLE );
                }
            }
        } );
        final EditText editNameChannel = view.findViewById( R.id.editCreateChannelName );
        final Button buttonCreateChannel = view.findViewById( R.id.buttonCreateChannel );
        buttonCreateChannel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String name = editNameChannel.getText().toString();
                if (ChannelNameIsTrue( name )) {
                    MaterialDialog mDialog = new MaterialDialog.Builder( getActivity() )
                            .setTitle( "Create?" )
                            .setMessage( "Are you sure want to create this Group  with name -" + name )
                            .setCancelable( false )
                            .setPositiveButton( "Yes", R.drawable.ic_add_black_24dp, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    ChannelService.createNewChannel( name, null, FirebaseAuth.getInstance().getCurrentUser().getUid(), nameOfCurrentUser );
                                    dialogInterface.dismiss();
                                    morphAnimationCreateChannel.morphIntoButton();
                                    buttonGoSearch.setVisibility( View.VISIBLE);
                                    channelView.setVisibility( View.VISIBLE );
                                    AdminChannelView.setVisibility( View.VISIBLE );

                                }
                            } )
                            .setNegativeButton( "Cancel", R.drawable.ic_clear_black_24dp, new MaterialDialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {
                                    dialogInterface.dismiss();
                                }
                            } )
                            .build();
                    mDialog.show();
                } else {
                    Toasty.error( getContext(), R.string.to_short_channel_error, Toasty.LENGTH_SHORT, true ).show();
                }


            }
        } );

        return view;
    }

    private boolean ChannelNameIsTrue(String name) {
        return name.length() > 3;
    }
    static class ChannelViewHolder extends RecyclerView.ViewHolder {
        final TextView ChannelNameTextView;

        ChannelViewHolder(@NonNull View itemView) {
            super( itemView );

            ChannelNameTextView = itemView.findViewById( R.id.ChannelNameTextView );
        }

        public void bind(Channel channel) {
            ChannelNameTextView.setText( channel.name );
        }
    }
    static class AdminChannelViewHolder extends RecyclerView.ViewHolder {
        final TextView ChannelNameTextView;

        AdminChannelViewHolder(@NonNull View itemView) {
            super( itemView );

            ChannelNameTextView = itemView.findViewById( R.id.ChannelNameTextView );
        }

        public void bind(Channel channel) {
            ChannelNameTextView.setText( channel.name );
        }
    }
    private void fetchAdmin() {
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


        adapterAdmin = new FirebaseRecyclerAdapter<Channel, AdminChannelViewHolder>( options ) {
            @Override
            public AdminChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.item_channel, parent, false );

                return new AdminChannelViewHolder( view );
            }


            @Override
            protected void onBindViewHolder(AdminChannelViewHolder holder, final int position, final Channel channel) {
                final TextView NameView = holder.ChannelNameTextView;
                NameView.setText( channel.getName() );
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ChannelFragment channelFragment = new ChannelFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("ChannelName",channel.getName() );
                        channelFragment.setArguments(bundle);
                        getChildFragmentManager().beginTransaction().add( R.id.my_channel_container,channelFragment ).addToBackStack(null).commit();

                    }
                } );
            }

        };
        AdminChannelView.setAdapter( adapterAdmin );
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() ).child( "Subchannels" );
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
                        getChildFragmentManager().beginTransaction().add( R.id.my_channel_container,channelFragment ).addToBackStack(null).commit();

                    }
                } );
            }

        };
        channelView.setAdapter( adapter );
    }

    void GoToFragment(String Fragment) {
        InterfaceFragment interfaceFragment = new InterfaceFragment();
        MyChannelsFragment myChannelsFragment = new MyChannelsFragment();
        SearchFragment searchFragment = new SearchFragment();
        ChannelFragment channelFragment = new ChannelFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (Fragment) {
            case "rChannel":
                fragmentManager.popBackStackImmediate();
                fragmentTransaction.remove( channelFragment);
                break;
            case "Search":
                fragmentTransaction.add( R.id.my_channel_container,searchFragment ).addToBackStack( null);
                break;

        }
        fragmentTransaction.commit();
    }


}


