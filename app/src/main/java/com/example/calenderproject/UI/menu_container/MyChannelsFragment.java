package com.example.calenderproject.UI.menu_container;

import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.ChannelService;
import com.example.calenderproject.objects.Channel;
import com.example.calenderproject.util.MorphAnimation;
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
import java.util.Map;

import es.dmoral.toasty.Toasty;
import me.samlss.broccoli.Broccoli;

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
    ImageButton buttonRemoveChannelRecycler;
    ImageButton buttonRemoveAdminChannelRecycler;
    HashMap<String,HashMap<String,String>> mapopa;
    private Map<View, Broccoli> mViewPlaceholderManager = new HashMap<>();


    private DatabaseReference refUser;
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

        refUser = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() );

        refUser.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mapopa = (HashMap) dataSnapshot.getValue();

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
        SharedPreferences settings = getContext().getSharedPreferences("ChannelCustomisation", 0);
        SharedPreferences.Editor editor = settings.edit();
        View view = inflater.inflate( R.layout.fragment_my_channels, container, false );
        final Button buttonGoCreate = view.findViewById( R.id.buttonToCreateChannel );
        final  Button buttonGoSearch = view.findViewById( R.id.buttonToSearch);
         buttonRemoveChannelRecycler = view.findViewById( R.id.buttonRemoveChannelRecycler );
         buttonRemoveAdminChannelRecycler = view.findViewById( R.id.buttonRemoveAdminChannelRecycler );
        ViewGroup CreateGroup = view.findViewById( R.id.create_channel_views );
        ViewGroup AdminChannelGroup = view.findViewById( R.id.admin_channel_recycler_views );
        ViewGroup ChannelGroup = view.findViewById( R.id.channel_recycler_views );
        final FrameLayout CreateLayout  = view.findViewById( R.id.createLayout );
        final FrameLayout ChannelLayout = view.findViewById( R.id.recyclerChannelLayout );
        final FrameLayout AdminChannelLayout = view.findViewById( R.id.recyclerAdminChannelLayout );
        final EditText editNameChannel = view.findViewById( R.id.editCreateChannelName );
        final Button buttonCreateChannel = view.findViewById( R.id.buttonCreateChannel );
        View CreateChannelContainer = view.findViewById( R.id.form_create_channel );
        View ChannelRecyclerContainer = view.findViewById( R.id.form_recycler_channel );
        View AdminChannelRecyclerContainer = view.findViewById( R.id.form_admin_channel_recycler );
        channelView = view.findViewById( R.id.ChannelView );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        channelView.setLayoutManager( linearLayoutManager );
        channelView.setHasFixedSize( true );
        AdminChannelView = view.findViewById( R.id.AdminChannelView );
        AdminLinearLayoutManager = new LinearLayoutManager( this.getActivity() );
        AdminChannelView.setLayoutManager( AdminLinearLayoutManager );
        AdminChannelView.setHasFixedSize( true );
        buttonGoSearch.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchFragment searchFragment = new SearchFragment();
                getParentFragmentManager().beginTransaction().replace( R.id.menu_container,searchFragment ).addToBackStack( null).commit();
            }
        } );
        final  MorphAnimation morphAnimationChannelRecycler = new MorphAnimation(ChannelRecyclerContainer,ChannelLayout,ChannelGroup  );
        final  MorphAnimation morphAnimationAdminChannelRecycler = new MorphAnimation( AdminChannelRecyclerContainer,AdminChannelLayout,AdminChannelGroup );
        buttonRemoveChannelRecycler.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (morphAnimationChannelRecycler.isPressed()) {
                    morphAnimationChannelRecycler.morphIntoButton();
                    buttonRemoveChannelRecycler.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    editor.putString("ChannelRecycler" ,"removed" );
                    editor.apply();
                } else {
                    morphAnimationChannelRecycler.morphIntoForm("MATCH_PARENT");
                    buttonRemoveChannelRecycler.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    editor.putString("AdminChannelRecycler" ,"added" );
                    editor.apply();
                }
            }
        } );
        buttonRemoveAdminChannelRecycler.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (morphAnimationAdminChannelRecycler.isPressed()) {
                    morphAnimationAdminChannelRecycler.morphIntoButton();
                    buttonRemoveAdminChannelRecycler.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    editor.putString("AdminChannelRecycler" ,"removed" );
                    editor.apply();
                } else {
                    morphAnimationAdminChannelRecycler.morphIntoForm("MATCH_PARENT");
                    buttonRemoveAdminChannelRecycler.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);;
                    editor.putString("AdminChannelRecycler" ,"added" );
                    editor.apply();
                }
            }
        } );
        final MorphAnimation morphAnimationCreateChannel = new MorphAnimation(CreateChannelContainer , CreateLayout, CreateGroup );
        buttonGoCreate.setOnClickListener( v -> {
            if (!morphAnimationCreateChannel.isPressed()) {
                buttonGoSearch.setVisibility( View.GONE);
                ChannelLayout.setVisibility( View.GONE );
                AdminChannelLayout.setVisibility( View.GONE );
                morphAnimationCreateChannel.morphIntoForm("MATCH_PARENT");
            } else {
                morphAnimationCreateChannel.morphIntoButton();
                buttonGoSearch.setVisibility( View.VISIBLE);
                ChannelLayout.setVisibility( View.VISIBLE );
                AdminChannelLayout.setVisibility( View.VISIBLE );
            }
        } );
        buttonCreateChannel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String name = editNameChannel.getText().toString();
                if (ChannelNameIsTrue( name )) {
                    MaterialDialog mDialog = new MaterialDialog.Builder( getActivity() )
                            .setTitle( "Create?" )
                            .setMessage( "Are you sure want to create this Group  with name -" + name )
                            .setCancelable( false )
                            .setPositiveButton( "Yes", R.drawable.ic_add_black_24dp, (dialogInterface, which) -> {
                                ChannelService.SetNewChannel(mapopa);
                                ChannelService.createNewChannel( name, null, FirebaseAuth.getInstance().getCurrentUser().getUid(), nameOfCurrentUser );
                                dialogInterface.dismiss();
                                morphAnimationCreateChannel.morphIntoButton();
                                buttonGoSearch.setVisibility( View.VISIBLE);
                                ChannelLayout.setVisibility( View.VISIBLE );
                                AdminChannelLayout.setVisibility( View.VISIBLE );

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
        fetch();
        fetchAdmin();


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
        Broccoli broccoli = new Broccoli();
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
                        bundle.putString("TypeOfChannel","groups");
                        channelFragment.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().replace( R.id.menu_container,channelFragment ).addToBackStack(null).commit();

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
                        bundle.putString("TypeOfChannel","Subchannels");
                        channelFragment.setArguments(bundle);
                        getParentFragmentManager().beginTransaction().replace( R.id.menu_container,channelFragment ).addToBackStack(null).commit();

                    }
                } );
            }

        };
        channelView.setAdapter( adapter );
    }

    @Override
    public void onResume() {
        super.onResume();
        buttonRemoveChannelRecycler.performClick();
        buttonRemoveChannelRecycler.performClick();
        buttonRemoveChannelRecycler.performClick();
    }
}


