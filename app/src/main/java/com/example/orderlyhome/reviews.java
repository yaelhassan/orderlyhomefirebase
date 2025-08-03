package com.example.orderlyhome;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class reviews implements Serializable {
    private String ID;
    private String Reviewer;
    private String Review;
    private String Reviewstars;
    private String organizerId;      // ← new

    public reviews(String reviewstars, String reviewer, String review) {
        this.Reviewer    = reviewer;
        this.Reviewstars = reviewstars;
        this.Review      = review;
        this.ID          = String.valueOf(System.currentTimeMillis());
    }

    public reviews(String reviewstars, String reviewer, String review, String id) {
        this.Reviewer    = reviewer;
        this.Reviewstars = reviewstars;
        this.Review      = review;
        this.ID          = id;
    }

    public String getReviewer()     { return Reviewer; }
    public String getReview()       { return Review; }
    public String getReviewstars1() { return Reviewstars; }
    public String getID()           { return ID; }

    // ← organizerId setter & getter
    public void setOrganizerId(String id) { this.organizerId = id; }
    public String getOrganizerId()       { return organizerId; }

    public Map<String,String> getAsMap() {
        Map<String,String> map = new HashMap<>();
        map.put("ID",          ID);
        map.put("Reviewer",    Reviewer);
        map.put("Reviewstars", Reviewstars);
        map.put("Review",      Review);
        map.put("organizerId", organizerId);
        return map;
    }
}
