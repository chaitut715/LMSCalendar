package com.chaitu.lmscalendar.ui;

/**
 * Created by Naresh on 11/30/2016.
 */

public class CalendarModel {
    int id;
    String name;


    public CalendarModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CalendarModel){
            CalendarModel c = (CalendarModel )obj;
            if(c.getName().equals(name) && c.getId()==id ) return true;
        }

        return false;
    }
}
