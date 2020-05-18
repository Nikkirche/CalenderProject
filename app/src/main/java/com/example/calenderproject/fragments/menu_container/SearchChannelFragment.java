package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    ;

    HashMap<String, HashMap<String, String>> SubEvents;
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
       // BackButton = view.findViewById( R.id.BackButton );
        NameView = view.findViewById( R.id.NameView );





        SubscribeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubscribeButton.setClickable(false);




    refUser = FirebaseDatabase.getInstance().getReference("users").child(GroupUser.getUid()).child("Subchannels");
    refUser.addValueEventListener(new ValueEventListener() {
                                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                           HashMap<String, String> map = (HashMap) dataSnapshot.getValue();



                                        //  if (map.get("Subchannels") != null) {
                                          //    data1 = map.get("Subchannels");

                                             map.put(channelName, channelName);
                                              //map.put("Subchannels", data1);
                                       //   } else {
                                              map.put(channelName, channelName);
                                          //    map.put("Subchannels", data1);
                                        //  }


                                          refUser.setValue(map).addOnCompleteListener(
                                                  new OnCompleteListener<Void>() {
                                                      @Override
                                                      public void onComplete(@NonNull Task<Void> rer2) {
                                                          if (rer2.isSuccessful()) {
                                                              Log.d("firebase", "dude");
                                                          }
                                                      }
                                                  }
                                          );
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

                reftoUser = FirebaseDatabase.getInstance().getReference("users").child(GroupUser.getUid()).child("events");

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

                });





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

