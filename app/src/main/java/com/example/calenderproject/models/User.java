package com.example.calenderproject.models;

import java.util.ArrayList;

public class User {
    public String email;
    public String name;
    public String id;
    public ArrayList<Channel> SubscribedChannels = new ArrayList<>();
    public User() {
    }

    public User(String email, String name,String id, ArrayList SubscribedChannels) {
        this.email = email;
        this.name = name;
        this.SubscribedChannels=SubscribedChannels;
        this.id=id;

    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", SubscribedChannels=" + SubscribedChannels +
                '}';
    }
}