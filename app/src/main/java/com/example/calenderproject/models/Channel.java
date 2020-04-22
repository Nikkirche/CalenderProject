package com.example.calenderproject.models;

import java.util.ArrayList;

public class Channel {
    ArrayList<User>  ChannelUsers = new ArrayList<>();
    String name;
    public Channel() {
    }


    public Channel(ArrayList<User> channelUsers, String name) {
        this.ChannelUsers = channelUsers;
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

