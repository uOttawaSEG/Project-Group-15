package com.example.math_tutor_application.div4;

import java.util.Date;

public class Sessions {
    private String documentId;
    private String approvedTutorId;
    private Date startDate;
    private Date endDate;
    private boolean upcomingSession;
    private boolean isStudentRegister;

    // Getters
    public String getDocumentId() { return documentId; }
    public String getApprovedTutorId() { return approvedTutorId; }
    public Date getStartDate() { return startDate; }
    public Date getEndDate() { return endDate; }
    public boolean isUpcomingSession() { return upcomingSession; }
    public boolean isStudentRegister() { return isStudentRegister; }

    // Setters
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public void setApprovedTutorId(String approvedTutorId) { this.approvedTutorId = approvedTutorId; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }
    public void setUpcomingSession(boolean upcomingSession) { this.upcomingSession = upcomingSession; }
    public void setStudentRegister(boolean studentRegister) { isStudentRegister = studentRegister; }
}

