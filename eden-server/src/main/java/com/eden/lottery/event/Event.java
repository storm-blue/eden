package com.eden.lottery.event;

import java.util.List;

public class Event {

    public Event(boolean showSpecialEffect, List<EventItem> items) {
        this.showSpecialEffect = showSpecialEffect;
        this.items = items;
    }

    private boolean showSpecialEffect;
    private List<EventItem> items;

    public boolean isShowSpecialEffect() {
        return showSpecialEffect;
    }

    public void setShowSpecialEffect(boolean showSpecialEffect) {
        this.showSpecialEffect = showSpecialEffect;
    }

    public List<EventItem> getItems() {
        return items;
    }

    public void setItems(List<EventItem> items) {
        this.items = items;
    }
}
