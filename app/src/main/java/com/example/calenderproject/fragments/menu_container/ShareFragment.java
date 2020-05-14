package com.example.calenderproject.fragments.menu_container;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.R;
import com.example.calenderproject.models.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import net.glxn.qrgen.android.QRCode;

public class ShareFragment extends Fragment {
    private RecyclerView QRView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;
    private final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onStart() {
        super.onStart();
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
        QRView = view.findViewById( R.id.QRView );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        QRView.setLayoutManager( linearLayoutManager );
        QRView.setHasFixedSize( true );
        fetch();
        return view;
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

    private void fetch() {
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
        QRView.setAdapter( adapter );
    }
}

