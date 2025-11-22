package com.example.math_tutor_application.uml_classes;


public class RegisteredSessions extends Sessions {
    //When a student registers to a Session, a RegisteredSession is created

    String status = "pending";
  
    String approvedStudentID; //for firebase

    Student student;

    String course;



    public RegisteredSessions() {}

    public RegisteredSessions(Sessions session)
    {
        super(session.getStartDate(), session.getEndDate(), session.getManualApproval() , session.getIsStudentRegister(), session.getApprovedTutorId(), session.getDocumentId());


    }



    //setters and getters
    public String getStatus() {
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

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }





}
