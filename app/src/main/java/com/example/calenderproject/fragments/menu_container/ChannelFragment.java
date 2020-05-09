package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ChannelFragment extends Fragment {
    TextView NameView;
    String ChannelName,SubberName;
    private static DatabaseReference refUser;
    private static FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference refChannel;
    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        if (bundle != null) {
            ChannelName = bundle.getString( "ChannelName" );

            NameView.setText( ChannelName );
        } else {
            NameView.setText( "Error" );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_channel, container, false );
        NameView = view.findViewById( R.id.ChannelFragmentNameView );
        ImageButton eventButton = view.findViewById( R.id.buttonToCreateEvent);
        ImageButton backButton = view.findViewById( R.id.buttonToMyChannelsfromChannel );
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
                                                         if(data1!=null)
                                                         {


                                                             map.put("subscribers",data1);}
                                                         else
                                                         {map.remove("subscribers");}
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

    private void GoToFragment(Fragment Fragment) {
        getChildFragmentManager().beginTransaction().add( R.id.ChannelContainer,Fragment  ).addToBackStack( null).commit();

    }
}