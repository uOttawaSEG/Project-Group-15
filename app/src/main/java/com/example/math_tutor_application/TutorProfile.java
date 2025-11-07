package com.example.math_tutor_application;

import java.util.ArrayList;

public class TutorProfile {

    private String highestDegree;

    //private int coursesOffered; imstead of asking for the number i can just do a list

    private ArrayList<String> coursesOffered;


    public TutorProfile(String highestDegree, ArrayList<String> coursesOffered) {
        this.highestDegree = highestDegree;
        this.coursesOffered = coursesOffered;
    }

    public TutorProfile() {
    } // REQUIRED FOR FIREBASE


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

    public void setCoursesOffered(ArrayList<String> coursesOffered) {
        this.coursesOffered = coursesOffered;
    }
}