package com.example.math_tutor_application.uml_classes;

import java.util.ArrayList;

public class ApprovedStudent extends Student {

    public ArrayList<Sessions> sessionsArrayList = new ArrayList<>();

    public ApprovedStudent() {

    }

    public ApprovedStudent(Student student) {
        super(student.getFirstName(), student.getLastName(), student.getEmail(), student.getPassword(), student.getPhoneNumber(), student.getRole(), student.getStatus(), student.getProgramOfStudy());
    }

    public ArrayList<Sessions> getSessionsArrayList() {
        return sessionsArrayList;
    }
    public void setSessionsArrayList(ArrayList<Sessions> sessionsArrayList) {
        this.sessionsArrayList = sessionsArrayList;
    }


}
