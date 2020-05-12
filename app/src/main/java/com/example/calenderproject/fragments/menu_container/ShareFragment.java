package com.example.calenderproject.fragments.menu_container;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.calenderproject.R;

import net.glxn.qrgen.android.QRCode;
public class ShareFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_share, container, false );
        Bitmap myBitmap = QRCode.from("blabla").bitmap();
        ImageView myImage = view.findViewById(R.id.imageView);
        myImage.setImageBitmap(myBitmap);
        return view;
    }
}
