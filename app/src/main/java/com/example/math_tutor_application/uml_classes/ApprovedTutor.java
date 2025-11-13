package com.example.math_tutor_application.uml_classes;

import java.util.ArrayList;

public class ApprovedTutor extends Tutor {

    private int numberOfRating;

    private double rating;

    public ArrayList<Sessions> sessionsArrayList;

    public ApprovedTutor() {

    }

    public ApprovedTutor(Tutor tutor) {
        super(tutor.getFirstName(), tutor.getLastName(), tutor.getEmail(), tutor.getPassword(), tutor.getPhoneNumber(), tutor.getRole(), tutor.getStatus(), tutor.getHighestDegree(), tutor.getCoursesOffered());
        sessionsArrayList = new ArrayList<>();

    }

    //added method for ratings
    public void addRating(double rating) {
        this.rating = rating;
        this.numberOfRating++;
        if (rating > 5) {
            rating = 5;
        } else if (rating < 0) {
            rating = 0;
        }

        this.rating = (this.rating + rating) / numberOfRating;
    }


    public ArrayList<Sessions> getSessionsArrayList() {
        return sessionsArrayList;
    }
    public void setSessionsArrayList(ArrayList<Sessions> sessionsArrayList) {
        this.sessionsArrayList = sessionsArrayList;
    }

    public int getNumberOfRating() {
        return numberOfRating;
    }
    public void setNumberOfRating(int numberOfRating) {
        this.numberOfRating = numberOfRating;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }


}
