package com.example.calenderproject.fragments.menu_container;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.HashMap;


public class CameraFragment extends Fragment {
    private CodeScanner mCodeScanner;
    private static final FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference refUser;
    private DatabaseReference refChannel;
    private DatabaseReference reftoUser;
    private boolean toScribeOrNotToScribe=true;

    private Button BackButton;
    private int i=11;
    private DatabaseReference refChannelBoy;
    private DatabaseReference refChannelGirl;

    HashMap<String, HashMap<String, String>> SubEvents;
    HashMap<String, HashMap<String, String>> mappa;
    HashMap<String, String> data1;
    HashMap<String, HashMap<String, String>> data2;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View root = inflater.inflate( R.layout.fragment_camera, container, false );
        CodeScannerView scannerView = root.findViewById( R.id.scanner_view );
        ImageButton goBack = root.findViewById( R.id.goBackFromCamera );
        goBack.setOnClickListener( view -> {
            final ShareFragment ShareFragment = ((ShareFragment)  CameraFragment.this.getParentFragment());
            InterfaceFragment interfaceFragment = (InterfaceFragment) ShareFragment.getParentFragment();
            interfaceFragment.showMenu();
            ShareFragment.buttonToCamera.setVisibility( View.VISIBLE );
            ShareFragment.getChildFragmentManager().popBackStack();
        } );
        mCodeScanner = new CodeScanner( activity, scannerView );
        mCodeScanner.setDecodeCallback( result -> activity.runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText( activity, result.getText(), Toast.LENGTH_SHORT ).show();
                String name = result.getText().substring( 8 );
                MaterialDialog mDialog = new MaterialDialog.Builder( getActivity() )
                        .setTitle( "Subscribe?" )
                        .setMessage( "Are you sure want to subscribe this Group  with name -" + name )
                        .setCancelable( false )
                        .setPositiveButton( "Yes", R.drawable.ic_add_black_24dp, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();

                                refChannel = FirebaseDatabase.getInstance().getReference("Channels").child(name);

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


                                refChannel = FirebaseDatabase.getInstance().getReference("Channels").child(name);

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
                                                                      data1.put(name, name);
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

                                                                  }


                                                              }
                                );


                                refChannelBoy = FirebaseDatabase.getInstance().getReference("Channels").child(name);
                                refChannelBoy.setValue(mappa);

                                refChannelGirl = FirebaseDatabase.getInstance().getReference("users").child(GroupUser.getUid());
                                refChannelGirl.child( "Subchannels" ).setValue(data1);
                                refChannelGirl.child( "events" ).setValue(data2);



                            }
                        } )
                        .setNegativeButton( "Cancel", R.drawable.ic_clear_black_24dp, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        } ).build();
                mDialog.show();
            }
        } ) );
        scannerView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        } );
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}
