package com.example.calenderproject.models;

public class Event {
    String text;
    String  data;

    public Event(String text, String data) {
        this.text = text;
        this.data = data;
    }

    public Event(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
