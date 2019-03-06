package com.example.iiitkota;

import java.util.HashMap;

class List {
    private String key;
    private String Student_Name;
    private String Student_ID;
    private HashMap<String, HashMap<String, String>> attendance;
    private HashMap<String, HashMap<String, String>> marks;

    //Getter for Key
    public String getKey() {
        return key;
    }

    //Setter for Key
    public void setKey(String key) {
        this.key = key;
    }

    public List() {
        //For firebase database parsing
    }

    //Getter for Attendance
    public HashMap<String, HashMap<String, String>> getAttendance() {
        return attendance;
    }

    //Setter for Attendance
    public void setAttendance(HashMap<String, HashMap<String, String>> attendance) {
        this.attendance = attendance;
    }

    //Getter for Marks
    public HashMap<String, HashMap<String, String>> getMarks() {
        return marks;
    }

    //Setter for Marks
    public void setMarks(HashMap<String, HashMap<String, String>> marks) {
        this.marks = marks;
    }

    //Getter for Student ID
    public String getStudent_ID() {
        return Student_ID;
    }

    //Setter for Student ID
    public void setStudent_ID(String student_ID) {
        Student_ID = student_ID;
    }

    //Getter for Student Name
    public String getStudent_Name() {
        return Student_Name;
    }

    //Setter for Student Name
    public void setStudent_Name(String student_Name) {
        Student_Name = student_Name;
    }
}
