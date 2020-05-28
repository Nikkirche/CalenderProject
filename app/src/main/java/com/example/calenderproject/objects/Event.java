package com.example.calenderproject.objects;

public class Event {
    private final String text;
    private String  data;

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

    public String getData() {
        return data;
    }
}
