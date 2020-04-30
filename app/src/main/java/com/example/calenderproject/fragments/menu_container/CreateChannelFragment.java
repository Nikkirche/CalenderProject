package com.example.calenderproject.fragments.menu_container;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.calenderproject.MainActivity;
import com.example.calenderproject.R;
import com.example.calenderproject.firebase.ChannelService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class CreateChannelFragment extends Fragment {

    private DatabaseReference ref;
    String nameOfCurrentUser;
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() );
        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
              nameOfCurrentUser= map.get("id").get( "name" );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_create_channel, container, false );
        ImageButton buttonGoBack = view.findViewById( R.id.buttonToMyChannelsFromCreate);
        final EditText editNameChannel = view.findViewById( R.id.editCreateChannelName );
        final Button buttonCreateChannel = view.findViewById( R.id.buttonCreateChannel );
        buttonCreateChannel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editNameChannel.getText().toString();
                if(name.length() >3) {
                    ChannelService.createNewChannel( name, null,FirebaseAuth.getInstance().getCurrentUser().getUid(),nameOfCurrentUser );
                    final  MyChannelsFragment myChannelsFragment = ((MyChannelsFragment)CreateChannelFragment.this.getParentFragment());
                    myChannelsFragment.GoToFragment( "MyChannels" );
                }
                else{
                    Toast.makeText( getContext(),"You must write a name longer as 3 symbols",Toast.LENGTH_SHORT );
                }
            }
        } );
        buttonGoBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyChannelsFragment myChannelsFragment = ((MyChannelsFragment)CreateChannelFragment.this.getParentFragment());
                myChannelsFragment.GoToFragment( "MyChannels" );
            }
        } );

        return view;
    }
}
