package com.example.iiitkota;

import java.util.HashMap;

class List {
    private String key;
    private String Student_Name;
    private String Student_ID;
    private HashMap<String, HashMap<String,String>> attendance;
    private HashMap<String,  HashMap<String,String>> marks;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List(String Id, String Name) {
        Student_ID = Id;
        Student_Name = Name;
    }

    public List() {

    }

    public HashMap<String,  HashMap<String,String>> getAttendance() {
        return attendance;
    }

    public void setAttendance(HashMap<String,HashMap<String,String>> attendance) {
        this.attendance = attendance;
    }

    public HashMap<String, HashMap<String,String>> getMarks() {
        return marks;
    }

    public void setMarks(HashMap<String, HashMap<String,String>> marks) {
        this.marks = marks;
    }

    public String getStudent_ID() {
        return Student_ID;
    }

    public void setStudent_ID(String student_ID) {
        Student_ID = student_ID;
    }

    public String getStudent_Name() {
        return Student_Name;
    }

    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }
}
