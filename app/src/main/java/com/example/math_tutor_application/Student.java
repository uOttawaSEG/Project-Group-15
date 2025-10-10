package com.example.math_tutor_application;

public class Student extends User{

    private String programOfStudy;

    public Student(String programOfStudy){

    }

    public Student() {

    }

    public Student(String firstName, String lastName, String email, String password, String phoneNumber, String programOfStudy) {
        super(firstName, lastName, email, password, phoneNumber);
        this.programOfStudy = programOfStudy;

    }

    //add getters
    public String getProgramOfStudy() {
        return programOfStudy;
    }

    public void setProgramOfStudy(String programOfStudy) {
        this.programOfStudy = programOfStudy;
    }




}
