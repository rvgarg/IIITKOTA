package com.example.iiitkota;

import java.util.HashMap;

class List {
    private String key;
    private String Student_Name;
    private String Student_ID;
    private HashMap<String, HashMap<String, String>> attendance;
    private HashMap<String, HashMap<String, String>> marks;

    public List(String Id, String Name) {
        Student_ID = Id;
        Student_Name = Name;
    }

    public List() {

    }

    public String getId() {
        return Student_ID;
    }

    public String getName() {
        return Student_Name;
    }
}
