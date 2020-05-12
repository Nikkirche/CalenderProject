package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;

import com.example.calenderproject.R;
import com.example.calenderproject.firebase.ChannelService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.cyanea.app.CyaneaFragment;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class CreateChannelFragment extends CyaneaFragment {

    private DatabaseReference ref;
    private String nameOfCurrentUser;

    @Override
    public void onStart() {
        super.onStart();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final MyChannelsFragment myChannelsFragment = ((MyChannelsFragment) CreateChannelFragment.this.getParentFragment());
        View view = inflater.inflate( R.layout.fragment_create_channel, container, false );
        ImageButton buttonGoBack = view.findViewById( R.id.buttonToMyChannelsFromCreate );
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
                                    myChannelsFragment.GoToFragment( "rCreateChannel" );
                                    dialogInterface.dismiss();

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
        buttonGoBack.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myChannelsFragment.GoToFragment( "rCreateChannel" );
            }
        } );

        return view;
    }

    private boolean ChannelNameIsTrue(String name) {
        return name.length() > 3;
    }
}
