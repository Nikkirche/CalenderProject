package com.example.calenderproject.UI.menu_container;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.example.calenderproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.util.HashMap;


public class CameraFragment extends Fragment {
    private CodeScanner mCodeScanner;
    private static final FirebaseUser GroupUser = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference refUser;
    private DatabaseReference refChannel;
    private DatabaseReference reftoUser;
    private boolean toScribeOrNotToScribe = true;

    private Button BackButton;
    private int i = 11;
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
            final ShareFragment ShareFragment = ((ShareFragment) CameraFragment.this.getParentFragment());
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
                String name =  result.getText().substring( 8 );
                Log.e( "test", String.valueOf( name.length() ) );
                MaterialDialog mDialog = new MaterialDialog.Builder( getActivity() )
                        .setTitle( "Subscribe?" )
                        .setMessage( "Are you sure want to subscribe this Group  with name -" + name )
                        .setCancelable( false )
                        .setPositiveButton( "Yes", R.drawable.ic_add_black_24dp, new MaterialDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                Log.e( "dd", "kakat" );
                                SearchChannelFragment searchChannelFragment = new SearchChannelFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString( "ChannelName", name );
                                searchChannelFragment.setArguments( bundle );
                                getParentFragmentManager().beginTransaction().add( R.id.ShareContainer, searchChannelFragment ).addToBackStack( null ).commit();


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
