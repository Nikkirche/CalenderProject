package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SearchChannelFragment extends Fragment {
    private TextView NameView;
    private static final FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference refUser;
    private DatabaseReference refChannel;
    private DatabaseReference reftoUser;
    private String channelName;
    private boolean toScribeOrNotToScribe=true;
    private ImageButton SubscribeButton;
    private Button BackButton;
    private int i=11;
    private DatabaseReference refChannelBoy;
    private DatabaseReference refChannelGirl;

    HashMap<String, HashMap<String, String>> SubEvents;
    HashMap<String, HashMap<String, String>> mappa;
    HashMap<String, String> data1;
    HashMap<String, HashMap<String, String>> data2;




    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            channelName = bundle.getString( "ChannelName" );
            NameView.setText( channelName );
        } else {
            NameView.setText( "Error" );
        }
        refUser = FirebaseDatabase.getInstance().getReference( "Channels" ).child( channelName );
        refUser.addValueEventListener(new ValueEventListener() {
                                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                              HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();

                                              HashMap<String, String> data = new HashMap<>();
                                              if (map.get("subscribers") != null) {
                                                  data = map.get("subscribers");
                                                  for(String key: data.keySet())
                                                  {
                                                      if(key.equals(GroupUser.getUid()))
                                                      {
                                                          toScribeOrNotToScribe=false;

                                                          SubscribeButton.setEnabled(false);
                                                          SubscribeButton.setVisibility(View.GONE);
                                                      }

                                                  }


                                              } else {

                                              }
                                          }
                                          @Override
                                          public void onCancelled(@NonNull DatabaseError databaseError) {
                                          }
                                      }
        );

        refChannel = FirebaseDatabase.getInstance().getReference("Channels").child(channelName);
        refChannel.addValueEventListener(new ValueEventListener() {
                                             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                 mappa = (HashMap) dataSnapshot.getValue();
                                                 HashMap<String, String> data11 = new HashMap<>();
                                                 if (mappa.get("subscribers") != null) {
                                                     data11 = mappa.get("subscribers");

                                                     data11.put(GroupUser.getUid(), GroupUser.getUid());
                                                     mappa.put("subscribers", data11);
                                                 } else {
                                                     data1.put(GroupUser.getUid(), GroupUser.getUid());
                                                     mappa.put("subscribers", data11);
                                                 }
                                             }
                                             @Override
                                             public void onCancelled(@NonNull DatabaseError databaseError) {
                                             }
                                         }
        );
        refChannel = FirebaseDatabase.getInstance().getReference("Channels").child(channelName);

        refChannel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String,HashMap<String, HashMap<String, String>>>map=(HashMap) dataSnapshot.getValue();
                SubEvents=map.get("events");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        refUser = FirebaseDatabase.getInstance().getReference("users").child(GroupUser.getUid());
        refUser.addValueEventListener(new ValueEventListener() {
                                          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                              HashMap<String,HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();
                                              data1 = map.get( "Subchannels" );
                                              data1.put(channelName, channelName);
                                              map.put("Subchannels", data1);
                                              HashMap<String,HashMap<String, HashMap<String, String>>>map1 =(HashMap) dataSnapshot.getValue();
                                              data2 =map1.get( "events" );

                                              if(SubEvents!=null)
                                              { for (String key : SubEvents.keySet()) {
                                                  data2.put(key, SubEvents.get(key));
                                              }}
                                          }
                                          @Override
                                          public void onCancelled(@NonNull DatabaseError databaseError) {
                                              SubscribeButton.setClickable(true);
                                          }
                                      }
        );

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_search_channel, container, false );
        SubscribeButton = view.findViewById( R.id.SubscribeButton );
        NameView = view.findViewById( R.id.NameView );





        SubscribeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubscribeButton.setClickable(false);

                refChannelBoy = FirebaseDatabase.getInstance().getReference("Channels").child(channelName);
                if (mappa!=null) {
                    refChannelBoy.setValue( mappa );
                }

                refChannelGirl = FirebaseDatabase.getInstance().getReference("users").child(GroupUser.getUid());
                if(data1!=null) {
                    refChannelGirl.child( "Subchannels" ).setValue( data1 );
                }
                if (data2!=null) {
                    refChannelGirl.child( "events" ).setValue( data2 );
                }
            }
        });



        return view;



    }


















    }

