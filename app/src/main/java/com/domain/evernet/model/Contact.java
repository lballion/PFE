package com.domain.evernet.model;

import androidx.annotation.NonNull;

public class Contact {

    private int id;
    private String name;

    public Contact(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Contact(this.id, this.name);

    }
}
