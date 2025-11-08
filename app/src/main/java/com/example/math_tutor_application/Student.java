package com.example.math_tutor_application;

import androidx.annotation.NonNull;

public class Student extends User {
    private String programOfStudy;



    public Student() {

    }

    public Student(String firstName, String lastName, String email, String password, String phoneNumber, String role, String status, String programOfStudy) {
        super(firstName, lastName, email, password, phoneNumber, role, status);

        this.programOfStudy = programOfStudy;
    }

    //add getters
    public String getProgramOfStudy() {
        return programOfStudy;
    }

    public void setProgramOfStudy(String programOfStudy) {
        this.programOfStudy = programOfStudy;
    }

    @NonNull
    @Override
   public String toString() {
        return "Student{" +
                super.toString() +
                "programOfStudy='" + programOfStudy + '\'' +
                '}';
    }

}
