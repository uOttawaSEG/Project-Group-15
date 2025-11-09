package com.example.math_tutor_application;


import com.google.firebase.Timestamp;

public class Sessions {

    private Timestamp startDate;

    private Timestamp endDate;

    private boolean manualApproval;

    private String documentId; //for firebase

    private String approvedTutorId; //for firebase



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

}
