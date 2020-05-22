package com.example.calenderproject.fragments.menu_container;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
public class SharePresenter {
    private ShareFragment view;
    private ShareModel model;

    SharePresenter(ShareFragment view) {
        this.view = view;
        this.model = new ShareModel();
    }

    void addCameraFragment() {
        view.setCameraFragment();

    }
    FirebaseRecyclerAdapter getData(){
        return  view.getAdapter(view.getOptions());
    }
}
