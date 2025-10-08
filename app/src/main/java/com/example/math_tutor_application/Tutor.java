package com.example.math_tutor_application;

public class Tutor extends User{
    //A class to represent a tutor. Inherits from User. Can be used to add ratings to tutors
    private double rating;
    private int numRatings;


    public Tutor(String userId, String password, String name, String role) {

        super(userId, password, name, role);

        if (!role.equals("tutor")) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

    }

    public double getRating() {
        return rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public void addRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("Invalid rating: " + rating);
        }
        numRatings++;
        this.rating = (this.rating * (numRatings - 1) + rating) / numRatings;
    }
}
