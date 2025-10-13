package com.example.math_tutor_application;

import java.util.ArrayList;

public class Tutor extends User{

    private String highestDegree;

    //private int coursesOffered; imstead of asking for the number i can just do a list

    private ArrayList<String> coursesOffered;



    public Tutor(String firstName, String lastName, String email, String password, String phoneNumber, String highestDegree, ArrayList<String> coursesOffered) {
        super(firstName, lastName, email, password, phoneNumber);
        this.highestDegree = highestDegree;
        this.coursesOffered = coursesOffered;
    }

    public Tutor() {} // REQUIRED FOR FIREBASE


    //add getters & Setters
    public String getHighestDegree() {
        return highestDegree;
    }

    public ArrayList<String> getCoursesOffered() {
        return coursesOffered;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public void setCoursesOffered(ArrayList<String> coursesOffered) { this.coursesOffered = coursesOffered; }

}
