package com.example.math_tutor_application;



public class User  {
    //an object to represent Users. Will contain thier userId, names and passwords.

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;

    public String status;

    private String role;

    public StudentProfile studentProfile;
    public TutorProfile tutorProfile;

    private String documentId;




    public User(String firstName, String lastName, String email, String password, String phoneNumber, String role, String status, StudentProfile studentProfile, TutorProfile tutorProfile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.status = status;
        this.role = role;
        this.studentProfile = studentProfile;
        this.tutorProfile = tutorProfile;

    }

    public User(){

    }

    //add getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getRole() {
        return role;
    }

    public StudentProfile getStudentProfile() {
        return studentProfile;
    }

    public TutorProfile getTutorProfile() {
        return tutorProfile;
    }

    public String getDocumentId() {
        return documentId;
    }




    //add setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public void setStudentProfile(StudentProfile studentProfile) {
        this.studentProfile = studentProfile;
    }
    public void setTutorProfile(TutorProfile tutorProfile) {
        this.tutorProfile = tutorProfile;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}
