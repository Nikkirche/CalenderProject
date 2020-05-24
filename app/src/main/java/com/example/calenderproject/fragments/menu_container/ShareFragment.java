package com.example.calenderproject.fragments.menu_container;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.R;
import com.example.calenderproject.models.Channel;
import com.example.calenderproject.presenter.SharePresenter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jaredrummler.cyanea.app.CyaneaFragment;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import net.glxn.qrgen.android.QRCode;

public class ShareFragment extends CyaneaFragment {
    private RecyclerView QRView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private SharePresenter sharePresenter;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


    @Override
    public void onStart() {
        super.onStart();
        sharePresenter = new SharePresenter( this );
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
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_share, container, false );
        Button buttonToCamera = view.findViewById( R.id.buttonToCameraFragment );
        buttonToCamera.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withContext( getContext()).withPermission( Manifest.permission.CAMERA ).withListener( new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        sharePresenter.addCameraFragment();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Need Permissions");
                            builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
                            builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    openSettings();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();

                    }
                } ).check();

            }
        } );
        QRView = view.findViewById( R.id.QRView );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        QRView.setLayoutManager( linearLayoutManager );
        QRView.setHasFixedSize( true );
        //adapter = sharePresenter.getData();
        adapter = getAdapter( getOptions() );
        QRView.setAdapter( adapter );
        return view;
    }


    public FirebaseRecyclerOptions getOptions() {
        Query query = FirebaseDatabase.getInstance().getReference( "users" ).child( firebaseUser.getUid() ).child( "groups" );
        FirebaseRecyclerOptions<Channel> options =
                new FirebaseRecyclerOptions.Builder<Channel>()
                        .setQuery( query, new SnapshotParser<Channel>() {
                            @NonNull
                            @Override
                            public Channel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Channel( snapshot.getValue().toString() );
                            }
                        } )
                        .build();
        return options;
    }

    public FirebaseRecyclerAdapter getAdapter(FirebaseRecyclerOptions options) {
        adapter = new FirebaseRecyclerAdapter<Channel, ShareFragment.ChannelViewHolder>( options ) {
            @Override
            public ShareFragment.ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.item_qr_share_channel, parent, false );

                return new ShareFragment.ChannelViewHolder( view );
            }


            @Override
            protected void onBindViewHolder(ShareFragment.ChannelViewHolder holder, final int position, final Channel channel) {
                final ImageView QRCod = holder.QRCod;
                Bitmap QR = QRCode.from( "Calender" + channel.getName() ).bitmap();
                QRCod.setImageBitmap( QR );
            }

        };
        return adapter;
    }


    public void setCameraFragment() {
        CameraFragment cameraFragment = new CameraFragment();
        getChildFragmentManager().beginTransaction().add( R.id.ShareContainer, cameraFragment ).commit();

    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {
        final ImageView QRCod;

        ChannelViewHolder(@NonNull View itemView) {
            super( itemView );

            QRCod = itemView.findViewById( R.id.QRCod );
        }

        public void bind(Channel channel) {
            Bitmap QR = QRCode.from( "Calender" + channel.getName() ).bitmap();
            QRCod.setImageBitmap( QR );
        }
    }
    public  void openSettings() {
        startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
    }


}

