package com.example.orderlyhome;

import java.io.Serializable;  // Importing the Serializable interface to allow this class to be passed between activities

public class reviews implements Serializable {

    // Declaring private variables for each property of a review
    private int Logo;           // This will store the logo image resource ID for the reviewer's profile picture
    private String Reviewer;    // This stores the name of the person who wrote the review
    private String Review;      // This stores the content of the review itself
    private String Reviewstars; // This stores the rating given by the reviewer (e.g., "4 stars")

    // Constructor to initialize the review object with its properties
    // This constructor takes in the rating (Reviewstars), logo (Logo), reviewer's name (Reviewer), and the review content (Review)
    public reviews(String reviewstars, int logo, String reviewer, String review) {
        this.Reviewstars = reviewstars;  // Initializing the Reviewstars with the provided value
        this.Logo = logo;                // Initializing the Logo with the provided value
        this.Reviewer = reviewer;        // Initializing the Reviewer's name
        this.Review = review;            // Initializing the Review content
    }

    // Getter method to access the logo image resource ID
    // Returns the logo value, which is an integer representing the image resource ID
    public int getLogo() {
        return Logo;
    }

    // Getter method to access the reviewer's name
    // Returns the name of the reviewer
    public String getReviewer() {
        return Reviewer;
    }

    // Getter method to access the review content
    // Returns the actual content of the review written by the reviewer
    public String getReview() {
        return Review;
    }

    // Getter method to access the review's star rating
    // Returns the rating given by the reviewer (e.g., "4 stars")
    public String getReviewstars1() {
        return Reviewstars;
    }
}
