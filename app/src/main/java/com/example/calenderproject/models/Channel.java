package com.example.calenderproject.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Channel {
    HashMap<String, HashMap<String, String>> ChannelUsers = new HashMap<>();
    public  String name;

    public Channel() {
    }


    public Channel(HashMap<String, HashMap<String, String>>  ChannelUsers, String name) {
        this.ChannelUsers = ChannelUsers;
        this.name = name;
    }

    public Channel(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Channels{" +
                "ChannelUsers=" + ChannelUsers +
                ", name='" + name + '\'' +
                '}';
    }
}

