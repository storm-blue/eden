package com.eden.lottery.event.scene;

import com.eden.lottery.event.Event;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    public Scene(List<String> users, String residence, List<Event> events) {
        this.users = users;
        this.residence = residence;
        this.events = events;
    }

    private List<String> users = new ArrayList<>();
    private String residence;
    private List<Event> events = new ArrayList<>();

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getResidence() {
        return residence;
    }

    public void setResidence(String residence) {
        this.residence = residence;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
