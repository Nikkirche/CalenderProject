package com.example.calenderproject.UI.menu_container;

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
import com.example.calenderproject.objects.Channel;
import com.example.calenderproject.objects.Event;
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

import java.util.HashMap;

public class ChannelFragment extends CyaneaFragment {
    private TextView NameView;
    private String ChannelName,SubberName;
    private String TypeOfChannel;
    private RecyclerView EventView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private static DatabaseReference refUser;
    private static DatabaseReference reftoUser;
    private static final FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference refChannel;
    private DatabaseReference reftoChannel;
    HashMap<String, HashMap<String, String>> map;
    HashMap<String, HashMap<String, String>> map1;
    private ImageButton eventButton;
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
        Bundle bundle = getArguments();
        if (bundle != null) {

            TypeOfChannel=bundle.getString("TypeOfChannel");
            if(TypeOfChannel.equals("Subchannels")
            )
            {eventButton.setEnabled(false);
                eventButton.setVisibility(View.GONE);}
            NameView.setText( ChannelName );
        } else {
            NameView.setText( "Error" );
        }
        refUser = FirebaseDatabase.getInstance().getReference( "users" ).child( GroupUser.getUid() );

        refUser.addValueEventListener( new ValueEventListener() {
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               map = (HashMap) dataSnapshot.getValue();
                                               HashMap<String, String> data1;
                                                HashMap<String, String> data2 = new HashMap<>();
                                              data1 = map.get( TypeOfChannel );


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
                                                   {map.put( TypeOfChannel, data1 );}
                                                   else
                                                   {  map.remove(TypeOfChannel);}
                                               }
                                               else{
                                                   map.remove(TypeOfChannel);
                                               }

                                               SubberName=map.get("id").get("name");




                                           }

                                           @Override
                                           public void onCancelled(@NonNull DatabaseError databaseError) {

                                           }


                                       }
        );
        refChannel = FirebaseDatabase.getInstance().getReference( "Channels" ).child(ChannelName  );
        refChannel.addValueEventListener( new ValueEventListener() {
                                              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                  map1 = (HashMap) dataSnapshot.getValue();

                                                  HashMap<String, String> data1 = new HashMap<>();
                                                  HashMap<String, String> data2 = new HashMap<>();
                                                  data1 = map1.get( "subscribers" );


                                                  //    if (data1 != null) {
                                                  //  for (String key : data1.keySet()) {

                                                  //String Subber = data1.get( key );
                                                  // if (!Subber.equals( SubberName )) {

                                                  if(data1!=null) {
                                                      data1.remove(GroupUser.getUid());//put( Subber, Subber );


                                                      map1.put("subscribers",data1);
                                                  }
                                                  //  }
                                                  //  }
                                                  //     }
                                                  else{ map1.remove( "subscribers" );}




                                              }

                                              @Override
                                              public void onCancelled(@NonNull DatabaseError databaseError) {

                                              }


                                          }
        );


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
         eventButton = view.findViewById( R.id.buttonToCreateEvent);
        ImageButton backButton = view.findViewById( R.id.buttonToMyChannelsfromChannel );
        EventView = view.findViewById( R.id.EventView );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        EventView.setLayoutManager( linearLayoutManager );
        EventView.setHasFixedSize( true );
        Bundle bundle = getArguments();
        if (bundle != null) {
            ChannelName = bundle.getString( "ChannelName" );

        } else {
            NameView.setText( "Error" );
        }
        fetch(ChannelName);
        backButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStackImmediate();
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
        UnsubscribeButton.setOnClickListener( view1 -> {

            reftoUser = FirebaseDatabase.getInstance().getReference( "users" ).child( GroupUser.getUid() );
            reftoUser.setValue( map );


            reftoChannel = FirebaseDatabase.getInstance().getReference( "Channels" ).child(ChannelName  );
            refChannel.setValue( map1 );
        } );
        return view;


    }
    static class EventViewHolder extends RecyclerView.ViewHolder {
        final TextView EventNameTextView;
        final TextView EventDataTextView;

        EventViewHolder(@NonNull View itemView) {
            super( itemView );
            EventNameTextView = itemView.findViewById( R.id.EventNameTextView );
            EventDataTextView = itemView.findViewById( R.id.EventDataTextView );
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
                                String data = snapshot.getValue().toString();
                                String[] dataSplited = data.split( "=" );
                                int lena = dataSplited.length;
                                String time1 =dataSplited[lena-1];
                                String event1=new String(  );
                                for (int i = 0; i < lena-1; i++) {
                                    event1 = event1+"=" + dataSplited[i];
                                }
                                String event=new String(  );
                                int lenofevent=event1.length();
                                for (int i = 2; i <lenofevent ; i++) {
                                    event=event+event1.charAt( i );
                                }

                                String time = "";
                                int lenoftime=time1.length();
                                for (int i = 0; i <lenoftime-1 ; i++) {
                                    time=time+time1.charAt( i );
                                }

                                return new Event(event,time);
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
                final TextView EventDataView = holder.EventDataTextView;
                EventNameView.setText( event.getText() );
                EventDataView.setText( event.getData() );
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
        getParentFragmentManager().beginTransaction().replace( R.id.menu_container,Fragment  ).addToBackStack( null).commit();

    }
}