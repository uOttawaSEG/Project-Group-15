package com.example.math_tutor_application;

public class AprovedTutor extends Tutor{

    private Sessions sessions;

    public AprovedTutor() {

    }

    public AprovedTutor (Tutor tutor, Sessions sessions) {

        super(tutor.getFirstName(), tutor.getLastName(), tutor.getEmail(), tutor.getPassword(), tutor.getPhoneNumber(), tutor.getRole(), tutor.getStatus(), tutor.getHighestDegree(), tutor.getCoursesOffered());
        this.sessions = sessions;

    }

    public Sessions getSessions() {
        return sessions;
    }

    public void setSessions(Sessions sessions) {
        this.sessions = sessions;
    }


}
