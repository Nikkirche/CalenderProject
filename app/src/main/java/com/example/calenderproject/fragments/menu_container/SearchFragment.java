package com.example.calenderproject.fragments.menu_container;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jaredrummler.cyanea.app.CyaneaFragment;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class SearchFragment extends CyaneaFragment {
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView SearchRecycler;
    private FirebaseRecyclerAdapter adapter;
    HashMap<String, String> values;
    private Query query;
    private SearchView searchView;
    private TextView test;
    private String SearchQuery = "";
    private String SubChannelName;
    private static final FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference refChannel;
    private DatabaseReference refUser;
    HashMap<String, HashMap<String, String>> SubEvents;

    @Override
    public void onStart() {
        super.onStart();
      /*  Bundle bundle = getArguments();
        if (bundle != null) {
            SubChannelName = bundle.getString( "ChannelName" );


        } else {
        }*/
        fetch( SearchQuery);
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
        searchView = view.findViewById( R.id.SearchQuery );
        SearchRecycler = view.findViewById( R.id.SearchView );
        linearLayoutManager = new LinearLayoutManager( this.getActivity() );
        SearchRecycler.setLayoutManager( linearLayoutManager );
        test = view.findViewById( R.id.testSearch );
        setUpSearchObservable();
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

    private void fetch(String SearchQuery) {
        query = FirebaseDatabase.getInstance()
                .getReference( "Channels" );
            Log.println( Log.DEBUG, "test", query.toString() );
        FirebaseRecyclerOptions<Channel> options =
                new FirebaseRecyclerOptions.Builder<Channel>()
                        .setQuery( query, new SnapshotParser<Channel>() {
                            @NonNull
                            @Override
                            public Channel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                String ChannelName = Objects.requireNonNull( snapshot.child( "id" ).child( "name" ).getValue() ).toString();
                                if (ChannelName.contains(SearchQuery)){
                                    return new Channel( ChannelName );
                                }
                                else{
                                    return new Channel( "admin-stuff-really-nothing" );
                                }

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
                if (!channel.getName().equals( "admin-stuff-really-nothing" )) {
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
                else{
                    final TextView NameView = holder.ChannelNameTextView;
                    NameView.setVisibility( View.GONE );
                }
            }

        };
        SearchRecycler.setAdapter( adapter );
    }

    private void setUpSearchObservable() {
        RxSearchObservable.fromView( searchView )
                .debounce( 300, TimeUnit.MILLISECONDS )
                .distinctUntilChanged()
                .subscribeOn( Schedulers.io() )
                .observeOn( AndroidSchedulers.mainThread() )
                .subscribe( new Consumer<String>() {
                    @Override
                    public void accept(String result) {
                        SearchQuery = result;
                        adapter.stopListening();
                        fetch( SearchQuery );
                        adapter.startListening();

                    }
                } );
    }


    public static class RxSearchObservable {

        static Observable<String> fromView(SearchView searchView) {

            final PublishSubject<String> subject = PublishSubject.create();

            searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String text) {
                    subject.onNext( text );
                    return true;
                }
            } );

            return subject;
        }
    }
}
