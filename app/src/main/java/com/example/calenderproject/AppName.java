package com.example.calenderproject;

import android.app.Application;

import com.jaredrummler.cyanea.Cyanea;

public class AppName extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Cyanea.init( this, getResources() );
    }
    }

