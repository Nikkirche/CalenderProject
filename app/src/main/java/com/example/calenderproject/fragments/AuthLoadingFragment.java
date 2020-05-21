package com.example.calenderproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AuthLoadingFragment extends Fragment {
    Button buttonStart;
    TextView Yayd;
    private DatabaseReference ref1;
    private DatabaseReference ref2;
    HashMap<String,HashMap<String, HashMap<String, String>>> values1;


    @Override
    public void onStart() {
        super.onStart();
        //код загрузки

        buttonStart.setVisibility( View.VISIBLE );

        try {
            Thread.sleep(15000);
            FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();

            ref1 = FirebaseDatabase.getInstance().getReference( "users" ).child( GroupUser.getUid() );
            ref1.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    HashMap<String,HashMap<String, HashMap<String, String>>> valuessss1;
                    valuessss1 = (HashMap)dataSnapshot.getValue();
                    values1=valuessss1;



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );



        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

       /* */

        View view = inflater.inflate( R.layout.fragment_auth_loading, container, false );

         buttonStart = view.findViewById( R.id.buttonStart );
        buttonStart.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread.sleep(5000);}
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                FirebaseUser GroupUser1 = FirebaseAuth.getInstance().getCurrentUser();
                HashMap<String, HashMap<String, String>> data1 = new HashMap<>();
                HashMap<String,String> nothing = new HashMap<>(  );
                nothing.put("Pashal Egg", "1970-01-12 23:23" );
                data1.put("PashalEgg69",nothing);
                values1.put("events",data1);
                ref2 = FirebaseDatabase.getInstance().getReference( "users" ).child( GroupUser1.getUid() );
                ref2.setValue(values1);
               // final MainActivity act = (MainActivity) getActivity();
            //    act.GoToFragment( "InterfaceFragment" );


            }  });
        return view;
    }
}
