package com.eden.lottery.event;

import java.util.ArrayList;
import java.util.List;

public class ResidenceEvent {

    public ResidenceEvent(List<ResidenceEventItem> items) {
        this.items = items;
    }

    private List<ResidenceEventItem> items = new ArrayList<>();

    public List<ResidenceEventItem> getItems() {
        return items;
    }

    public void setItems(List<ResidenceEventItem> items) {
        this.items = items;
    }
}
