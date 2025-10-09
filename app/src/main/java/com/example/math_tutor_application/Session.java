package com.example.math_tutor_application;

import java.io.Serializable;
import java.util.Date;


public class Session implements Serializable {

    //A class that represents a session.

    private static final long serialVersionUID = 1L;


    private String studentName;
    private Date sessionTime;
    private String tutorName;
    private Boolean isCancelled;

    private String course;

    public Session(Date sessionTime, String course) {
        this.sessionTime = sessionTime;
        this.course = course;
        this.isCancelled = false;
    }

    public boolean cancelSession() {
        Date now = new Date();

        if (sessionTime.getTime()- now.getTime() > 1000*60*60) {
            isCancelled = true;
        } else {
            System.out.println("Session cannot be cancelled before 60 minutes.");
        }

        return isCancelled;
    }

    public void setSessionTime(Date sessionTime) {
        this.sessionTime = sessionTime;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public String getStudentName() {
        return studentName;
    }

    public Date getSessionTime() {
        return sessionTime;
    }

    public String getTutorName() {
        return tutorName;
    }

    public String getCourse() {
        return course;
    }


    public Boolean getIsCancelled() {
        return isCancelled;

    }




}
