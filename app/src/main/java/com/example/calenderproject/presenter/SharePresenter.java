package com.example.calenderproject.presenter;

import com.example.calenderproject.fragments.menu_container.ShareFragment;
import com.example.calenderproject.fragments.menu_container.ShareModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
public class SharePresenter {
    private ShareFragment view;
    private ShareModel model;

    public SharePresenter(ShareFragment view) {
        this.view = view;
        this.model = new ShareModel();
    }

    public void addCameraFragment() {
        view.setCameraFragment();

    }
    FirebaseRecyclerAdapter getData(){
        return  view.getAdapter(view.getOptions());
    }
}
