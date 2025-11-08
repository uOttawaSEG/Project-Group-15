package com.example.math_tutor_application;

import java.util.Date;

public class Sessions {

    private Date startDate;

    private Date endDate;

    private boolean manualApproval;

    private String documentId;



    public Sessions() {};

    public Sessions(Date startDate, Date endDate, boolean manualApproval) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.manualApproval = manualApproval;
    }

    //getters and setters


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }


    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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

}
