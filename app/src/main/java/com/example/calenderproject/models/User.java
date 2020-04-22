package com.example.calenderproject.models;

import java.util.ArrayList;

public class User {
    public String email;
    public String name;
    public ArrayList<Channel> SubscribedChannels = new ArrayList<>();
    public User() {
    }

    public User(String email, String name, ArrayList SubscribedChannels) {
        this.email = email;
        this.name = name;
        this.SubscribedChannels=SubscribedChannels;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}