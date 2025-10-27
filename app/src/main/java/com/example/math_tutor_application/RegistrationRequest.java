package com.example.math_tutor_application;

public class RegistrationRequest {
    private String role; // student or tutor
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    // Will only be used for registration requested of students
    private String programOfStudy;

    // Will only be used for registration requested of tutors
    private String coursesOffered;

    private String documentId;

    private boolean status;


    public RegistrationRequest(String role, String firstName, String lastName, String email, String phoneNumber, String programOfStudy, String coursesOffered, boolean status) {
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.programOfStudy = programOfStudy;
        this.coursesOffered = coursesOffered;
        this.status = status;
    }

    public RegistrationRequest() {}


    // Getters
    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getProgramOfStudy() { return programOfStudy; }
    public String getCoursesOffered() { return coursesOffered; }
    public boolean getStatus() { return status; }

    // Setters
    public void setRole(String role) { this.role = role; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setProgramOfStudy(String programOfStudy) { this.programOfStudy = programOfStudy; }
    public void setCoursesOffered(String coursesOffered) { this.coursesOffered = coursesOffered; }
    public void setStatus(boolean status) { this.status = status; }

     public String getDocumentId() {
        return documentId;
    }

     public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
}

