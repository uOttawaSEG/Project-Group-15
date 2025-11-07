package com.example.math_tutor_application;

public class StudentProfile {

    private String programOfStudy;



    public StudentProfile() {

    }

    public StudentProfile(String programOfStudy) {
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
