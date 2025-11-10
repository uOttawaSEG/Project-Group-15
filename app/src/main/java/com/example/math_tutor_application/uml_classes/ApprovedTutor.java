package com.example.math_tutor_application.uml_classes;

import java.util.ArrayList;

public class ApprovedTutor extends Tutor {

    public ArrayList<Sessions> sessionsArrayList;

    public ApprovedTutor() {

    }

    public ApprovedTutor(Tutor tutor) {
        super(tutor.getFirstName(), tutor.getLastName(), tutor.getEmail(), tutor.getPassword(), tutor.getPhoneNumber(), tutor.getRole(), tutor.getStatus(), tutor.getHighestDegree(), tutor.getCoursesOffered());
        sessionsArrayList = new ArrayList<>();

    }


    public ArrayList<Sessions> getSessionsArrayList() {
        return sessionsArrayList;
    }
    public void setSessionsArrayList(ArrayList<Sessions> sessionsArrayList) {
        this.sessionsArrayList = sessionsArrayList;
    }


}
