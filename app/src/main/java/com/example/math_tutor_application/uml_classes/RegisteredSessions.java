package com.example.math_tutor_application.uml_classes;


public class RegisteredSessions extends Sessions {
    //When a student registers to a Session, a RegisteredSession is created

    String status = "pending";
  
    String approvedStudentID; //for firebase

    Student student;



    public RegisteredSessions() {}

    public RegisteredSessions(Sessions session)
    {
        super(session.getStartDate(), session.getEndDate(), session.getManualApproval());

    }



    //setters and getters
    public String getstatus() {
        return status;
    }

    public void setStatus(String approved) {
        status = approved;
    }

    public String getApprovedStudentID() {
        return approvedStudentID;
    }



    public void setApprovedStudentID(String approvedStudentID) {
        this.approvedStudentID = approvedStudentID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }





}
