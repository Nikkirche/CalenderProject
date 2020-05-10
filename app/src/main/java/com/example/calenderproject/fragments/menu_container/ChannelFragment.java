package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.R;
import com.example.calenderproject.models.Channel;
import com.example.calenderproject.models.Event;
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

import java.util.HashMap;

public class ChannelFragment extends Fragment {
    private TextView NameView;
    private String ChannelName,SubberName;
    private RecyclerView EventView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private static DatabaseReference refUser;
    private static FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference refChannel;
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
        View view = inflater.inflate( R.layout.fragment_channel, container, false );
        NameView = view.findViewById( R.id.ChannelFragmentNameView );
        ImageButton eventButton = view.findViewById( R.id.buttonToCreateEvent);
        ImageButton backButton = view.findViewById( R.id.buttonToMyChannelsfromChannel );
        EventView = view.findViewById( R.id.EventView );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        EventView.setLayoutManager( linearLayoutManager );
        EventView.setHasFixedSize( true );
        Bundle bundle = getArguments();
        if (bundle != null) {
            ChannelName = bundle.getString( "ChannelName" );

            NameView.setText( ChannelName );
        } else {
            NameView.setText( "Error" );
        }
        fetch(ChannelName);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MyChannelsFragment myChannelsFragment = ((MyChannelsFragment)ChannelFragment.this.getParentFragment());
                myChannelsFragment.GoToFragment( "rChannel" );
            }
        } );
        eventButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEventFragment createEventFragment = new CreateEventFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ChannelName",NameView.getText().toString() );
                createEventFragment.setArguments(bundle);
                GoToFragment(createEventFragment);
            }
        } );
        ImageButton UnsubscribeButton = view.findViewById( R.id.buttonUnsubscribe );
        UnsubscribeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refUser = FirebaseDatabase.getInstance().getReference( "users" ).child( GroupUser.getUid() );

                refUser.addValueEventListener( new ValueEventListener() {
                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                       HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();
                                                       HashMap<String, String> data1;
                                                       HashMap<String, String> data2 = new HashMap<>();
                                                       data1 = map.get( "groups" );


                                                       /*if (data1 != null) {
                                                           for (String key : data1.keySet()) {

                                                               String TrueKey = data1.get( key );
                                                               if (!TrueKey.equals( ChannelName )) {
                                                                   data2.put( TrueKey, TrueKey );
                                                               }
                                                           }
                                                       }*/
                                                       if (data1 != null) {
                                                           data1.remove(ChannelName);
                                                       if(data1!=null)
                                                       {map.put( "groups", data1 );}
                                                       else
                                                       {  map.remove("groups");}
                                                       }
                                                       else{
                                                           map.remove("groups");
                                                       }

                                                       SubberName=map.get("id").get("name");


                                                       refUser.setValue( map );

                                                   }

                                                   @Override
                                                   public void onCancelled(@NonNull DatabaseError databaseError) {

                                                   }


                                               }
                );
                refChannel = FirebaseDatabase.getInstance().getReference( "Channels" ).child(ChannelName  );
                refChannel.addValueEventListener( new ValueEventListener() {
                                                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                       HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();

                                                       HashMap<String, String> data1 = new HashMap<>();
                                                       HashMap<String, String> data2 = new HashMap<>();
                                                       data1 = map.get( "subscribers" );


                                                   //    if (data1 != null) {
                                                         //  for (String key : data1.keySet()) {

                                                               //String Subber = data1.get( key );
                                                              // if (!Subber.equals( SubberName )) {

                                                     if(data1!=null) {
                                                         data1.remove(GroupUser.getUid());//put( Subber, Subber );


                                                         map.put("subscribers",data1);
                                                     }
                                                             //  }
                                                         //  }
                                                  //     }
                                                      else{ map.remove( "subscribers" );}


                                                       refChannel.setValue( map );

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
    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView EventNameTextView;

        public EventViewHolder(@NonNull View itemView) {
            super( itemView );

            EventNameTextView = itemView.findViewById( R.id.EventNameTextView );
        }

        public void bind(Channel channel) {
            EventNameTextView.setText( channel.name );
        }
    }

    private void fetch(String ChannelName) {
        Query query = FirebaseDatabase.getInstance().getReference( "Channels" ).child( ChannelName ).child( "events" );
        FirebaseRecyclerOptions<Event> options =
                new FirebaseRecyclerOptions.Builder<Event>()
                        .setQuery(query, new SnapshotParser<Event>() {
                            @NonNull
                            @Override
                            public Event parseSnapshot(@NonNull DataSnapshot snapshot) {

                                return new Event(snapshot.getValue().toString());
                            }
                        })
                        .build();


        adapter = new FirebaseRecyclerAdapter<Event, ChannelFragment.EventViewHolder>( options ) {
            @Override
            public ChannelFragment.EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.item_event, parent, false );

                return new ChannelFragment.EventViewHolder( view );
            }


            @Override
            protected void onBindViewHolder(ChannelFragment.EventViewHolder holder, final int position, final Event event) {
                final TextView EventNameView = holder.EventNameTextView;
                EventNameView.setText( event.getText() );
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*ChannelFragment channelFragment = new ChannelFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("ChannelName",channel.getName() );
                        channelFragment.setArguments(bundle);
                        getChildFragmentManager().beginTransaction().add( R.id.my_channel_container,channelFragment ).addToBackStack(null).commit();*/

                    }
                } );
            }

        };
        EventView.setAdapter( adapter );
    }

    private void GoToFragment(Fragment Fragment) {
        getChildFragmentManager().beginTransaction().add( R.id.ChannelContainer,Fragment  ).addToBackStack( null).commit();

    }
}