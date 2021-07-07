package com.batdaulaptrinh.completedictionary;

import androidx.annotation.NonNull;

public class Category {
    public static final int MEANING = 1;
    public static final int SYNONYM = 2;
    public static final int PRONUNCIATION = 3;
    private int id;
    private String name;
    public Category() {
    }
    public Category(String name) {
        this.name = name;
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
    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
