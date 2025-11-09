package com.example.math_tutor_application;


import com.google.firebase.Timestamp;

public class SessionRequest {
    private String documentId;
    private String approvedStudentID;
    private String approvedTutorId;
    private Timestamp startDate;
    private Timestamp endDate;
    private String status;

    public SessionRequest() {}

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getApprovedStudentID() { return approvedStudentID; }
    public void setApprovedStudentID(String approvedStudentID) { this.approvedStudentID = approvedStudentID; }

    public String getApprovedTutorId() { return approvedTutorId; }
    public void setApprovedTutorId(String approvedTutorId) { this.approvedTutorId = approvedTutorId; }

    public Timestamp getStartDate() { return startDate; }
    public void setStartDate(Timestamp startDate) { this.startDate = startDate; }

    public Timestamp getEndDate() { return endDate; }
    public void setEndDate(Timestamp endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
