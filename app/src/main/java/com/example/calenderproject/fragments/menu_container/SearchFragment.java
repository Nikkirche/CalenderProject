package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calenderproject.R;
import com.example.calenderproject.models.Channel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import java.util.HashMap;

public class SearchFragment extends CyaneaFragment {
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView SearchView;
    private FirebaseRecyclerAdapter adapter;
    HashMap<String, String> values;
    private String SearchQuery;
    private Query query;

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
        View view = inflater.inflate( R.layout.fragment_search, container, false );
        SearchView = view.findViewById( R.id.SearchView );
        final EditText EditSearchQuery = view.findViewById( R.id.SearchQuery );
        final ImageButton SearchButton = view.findViewById( R.id.SearchButton );
        SearchButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchQuery = EditSearchQuery.getText().toString();
            }
        } );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        SearchView.setLayoutManager( linearLayoutManager );
        SearchView.setHasFixedSize( true );
        fetch( SearchQuery );

        return view;
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {
        final TextView ChannelNameTextView;

        ChannelViewHolder(@NonNull View itemView) {
            super( itemView );

            ChannelNameTextView = itemView.findViewById( R.id.ChannelNameTextView );
        }

        public void bind(Channel channel) {
            ChannelNameTextView.setText( channel.name );
        }
    }

    private void fetch(final String SearchQuery) {
            query = FirebaseDatabase.getInstance()
                    .getReference("Channels").orderByChild( "name" ).equalTo( SearchQuery );
        FirebaseRecyclerOptions<Channel> options =
                new FirebaseRecyclerOptions.Builder<Channel>()
                        .setQuery( query, new SnapshotParser<Channel>() {
                            @NonNull
                            @Override
                            public Channel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                /*HashMap<String,HashMap<String, HashMap<String,String>>> map1=(HashMap) snapshot.getValue();
                                if (map1 != null ) {
                                    for (String key1 : map1.keySet()) {
                                        values.put(key1,key1);
                                    }
                                }*/
                                String ChannelName  = snapshot.child( "id" ).child( "name" ).getValue().toString();
                                    return new Channel( ChannelName );

                            }
                        } )
                        .build();


        adapter = new FirebaseRecyclerAdapter<Channel, SearchFragment.ChannelViewHolder>( options ) {
            @Override
            public SearchFragment.ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from( parent.getContext() )
                        .inflate( R.layout.item_channel, parent, false );

                return new SearchFragment.ChannelViewHolder( view );
            }


            @Override
            protected void onBindViewHolder(SearchFragment.ChannelViewHolder holder, final int position, final Channel channel) {
                final TextView NameView = holder.ChannelNameTextView;
                NameView.setText( channel.getName() );
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SearchChannelFragment searchChannelFragment = new SearchChannelFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString( "ChannelName", channel.getName() );
                        searchChannelFragment.setArguments( bundle );
                        getChildFragmentManager().beginTransaction().add( R.id.SearchContainer, searchChannelFragment ).addToBackStack( null ).commit();

                    }
                } );
            }

        };
        SearchView.setAdapter( adapter );
    }
}
