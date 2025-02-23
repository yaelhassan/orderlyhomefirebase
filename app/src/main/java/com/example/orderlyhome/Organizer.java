package com.example.orderlyhome;

import java.io.Serializable;
import java.util.List;

// The Organizer class represents an organizer with their personal information, fee, description, reviews, and images.
public class Organizer implements Serializable {

    // Fields to store information about the organizer
    private String name;               // The name of the organizer
    private String location;           // The location of the organizer
    private String fee;                // The hourly fee for the organizer's service
    private String description;        // A brief description about the organizer
    private int age;                   // The age of the organizer
    private int imageResId;            // The resource ID for the organizer's profile picture
    private int starsImageResId;       // The resource ID for the star rating image of the organizer
    private int portfolioPic1ResId;    // The resource ID for the first portfolio picture
    private int portfolioPic2ResId;    // The resource ID for the second portfolio picture
    private List<reviews> review;      // A list of reviews related to the organizer

    // Constructor to initialize the Organizer object with all the required information
    public Organizer(String name, String location, String fee, String description, int age,
                     int imageResId, int starsImageResId, int portfolioPic1ResId, int portfolioPic2ResId, List<reviews> review) {
        this.name = name;
        this.location = location;
        this.fee = fee;
        this.description = description;
        this.age = age;
        this.imageResId = imageResId;
        this.starsImageResId = starsImageResId;
        this.portfolioPic1ResId = portfolioPic1ResId;
        this.portfolioPic2ResId = portfolioPic2ResId;
        this.review = review;
    }

    // Getter methods to access each field of the Organizer class
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getFee() { return fee; }
    public String getDescription() { return description; }
    public int getAge() { return age; }
    public int getImageResId() { return imageResId; }
    public int getStarsImageResId() { return starsImageResId; }
    public int getPortfolioPic1ResId() { return portfolioPic1ResId; }
    public int getPortfolioPic2ResId() { return portfolioPic2ResId; }

    // Getter method to access the list of reviews for the organizer
    public List<reviews> getReview() {
        return review;
    }
}
