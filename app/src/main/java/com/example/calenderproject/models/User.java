package com.example.calenderproject.models;

import java.util.ArrayList;

class User {
    private String email;
    private String name;
    private String id;
    private ArrayList<Channel> SubscribedChannels = new ArrayList<>();
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