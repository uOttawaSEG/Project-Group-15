package com.example.math_tutor_application.uml_classes;


import com.google.firebase.Timestamp;

public class Sessions {

    private Timestamp startDate;

    private Timestamp endDate;

    private boolean manualApproval;

    private String documentId; //for firebase

    private String approvedTutorId; //for firebase

    ApprovedTutor approvedTutor;



    public Sessions() {};

    public Sessions(Timestamp startDate, Timestamp endDate, boolean manualApproval) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.manualApproval = manualApproval;
    }

    //getters and setters


    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }


    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public boolean getManualApproval() {
        return manualApproval;
    }

    public void setManualApproval(boolean manualApproval) {
        this.manualApproval = manualApproval;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getApprovedTutorId() {
        return approvedTutorId;
    }

    public void setApprovedTutorId(String approvedTutorId) {
        this.approvedTutorId = approvedTutorId;
    }

    public ApprovedTutor getApprovedTutor() {
        return approvedTutor;
    }
    public void setApprovedTutor(ApprovedTutor approvedTutor) {
        this.approvedTutor = approvedTutor;
    }


    // To check if a method is in the past or not
    public boolean isPastSession() {
        // If endDate is null then the sesh was invalid to begin with
        if (endDate == null) return false;

        // current time (right now = rn)
        java.util.Date rn = new java.util.Date();

        // Check if endDate is before current time
        return endDate.toDate().before(rn);
    }

    // For upcoming sessions to check
    public boolean isUpcomingSession() {

        if (startDate == null) return false;
        java.util.Date rn = new java.util.Date();
        return startDate.toDate().after(rn);
    }

}
