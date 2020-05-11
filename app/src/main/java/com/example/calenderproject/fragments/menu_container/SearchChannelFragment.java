package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private String channelName;
    private boolean toScribeOrNotToScribe=true;
    private ImageButton SubscribeButton;
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

                                              HashMap<String, String> data1 = new HashMap<>();



                                              if (map.get("subscribers") != null) {
                                                  data1 = map.get("subscribers");
                                                  for(String key: data1.keySet())
                                                  {
                                                      if(key.equals(GroupUser.getUid()))
                                                      {
                                                          toScribeOrNotToScribe=false;

                                                          SubscribeButton.setEnabled(false);
                                                          SubscribeButton.setVisibility(View.GONE);
                                                      }

                                                  }


                                              } else {
                                                  toScribeOrNotToScribe=true;
                                              }



                                          }

                                          @Override
                                          public void onCancelled(@NonNull DatabaseError databaseError) {

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




    refUser = FirebaseDatabase.getInstance().getReference("users").child(GroupUser.getUid());
    refUser.addValueEventListener(new ValueEventListener() {
                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                          HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();

                                          HashMap<String, String> data1 = new HashMap<>();
                                          ;
                                          HashMap<String, String> data2 = new HashMap<>();

                                          if (map.get("Subchannels") != null) {
                                              data1 = map.get("Subchannels");

                                              data1.put(channelName, channelName);
                                              map.put("Subchannels", data1);
                                          } else {
                                              data1.put(channelName, channelName);
                                              map.put("Subchannels", data1);
                                          }


                                          refUser.setValue(map);
                                      }

                                      @Override
                                      public void onCancelled(@NonNull DatabaseError databaseError) {
                                       SubscribeButton.setClickable(true);
                                      }


                                  }
    );


    refChannel = FirebaseDatabase.getInstance().getReference("Channels").child(channelName);

    refChannel.addValueEventListener(new ValueEventListener() {
                                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                             HashMap<String, HashMap<String, String>> map = (HashMap) dataSnapshot.getValue();

                                             HashMap<String, String> data1 = new HashMap<>();
                                             ;
                                             HashMap<String, String> data2 = new HashMap<>();

                                             if (map.get("subscribers") != null) {
                                                 data1 = map.get("subscribers");

                                                 data1.put(GroupUser.getUid(), GroupUser.getUid());
                                                 map.put("subscribers", data1);
                                             } else {
                                                 data1.put(GroupUser.getUid(), GroupUser.getUid());
                                                 map.put("subscribers", data1);
                                             }


                                             refChannel.setValue(map);
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

