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
       // BackButton = view.findViewById( R.id.BackButton );
        NameView = view.findViewById( R.id.NameView );





        SubscribeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubscribeButton.setClickable(false);

                refChannelBoy = FirebaseDatabase.getInstance().getReference("Channels").child(channelName);
                refChannelBoy.setValue(mappa);

                refChannelGirl = FirebaseDatabase.getInstance().getReference("users").child(GroupUser.getUid());
                refChannelGirl.child( "Subchannels" ).setValue(data1);
                refChannelGirl.child( "events" ).setValue(data2);






                /*reftoUser = FirebaseDatabase.getInstance().getReference("users").child(GroupUser.getUid()).child("events");

                reftoUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, HashMap<String, String>>map1 =(HashMap) dataSnapshot.getValue();

                      //  if(map1.get("events")!=null) {
                       //     HashMap<String, HashMap<String, String>> mappa=map1.get("events");
                            if(SubEvents!=null)
                            { for (String key : SubEvents.keySet()) {
                                map1.put(key, SubEvents.get(key));
                            }}
                         //   map1.put("events",mappa);
                 //       }
                 //       else
                  //      {
                  //          map1.put("events",SubEvents);
                   //     }



                        reftoUser.setValue(map1).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> rer1) {
                                        if (rer1.isSuccessful()) {
                                            Log.d("firebase", "dude");
                                        }
                                    }
                                }
                        );
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });*/





            }
        });

      /*  BackButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubscribeButton.setClickable(false);


                            SearchFragment searchFragment = new SearchFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString( "ChannelName", channelName );
                            searchFragment.setArguments( bundle );
                            getChildFragmentManager().beginTransaction().add( R.id.SearchContainer, searchFragment ).addToBackStack( null ).commit();



            }
        }
        );*/



        return view;



    }


















    }

