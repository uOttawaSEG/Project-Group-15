package com.example.math_tutor_application;

public class Tutor extends User{

    private String highestDegree;

    //private int coursesOffered; imstead of asking for the number i can just do a list

    private String[] coursesOffered;



    public Tutor(String firstName, String lastName, String email, String password, String phoneNumber, String highestDegree, String[] coursesOffered) {
        super(firstName, lastName, email, password, phoneNumber);
        this.highestDegree = highestDegree;
        this.coursesOffered = coursesOffered;
    }


    //add getters & Setters
    public String getHighestDegree() {
        return highestDegree;
    }

    public String[] getCoursesOffered() {
        return coursesOffered;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public void setCoursesOffered(String[] coursesOffered) {
        this.coursesOffered = coursesOffered;
    }

}
