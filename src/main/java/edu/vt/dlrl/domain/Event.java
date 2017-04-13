package edu.vt.dlrl.domain;

/**
 * Author: dedocibula
 * Created on: 13.4.2017.
 */
public class Event {
    private String id;
    private String name;
    private boolean selected;

    public Event(String id, String name) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
