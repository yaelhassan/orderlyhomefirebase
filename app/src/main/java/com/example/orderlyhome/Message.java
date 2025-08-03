package com.example.orderlyhome;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * A chat message, which may be text or an image.
 */
public class Message implements Serializable {
    private final String id;         // Unique identifier (we use timestamp string)
    private final String text;       // null if this is an image‑only message
    private final String imageUri;   // null if this is a text‑only message
    private final boolean sentByUser;
    private final long timestamp;    // ms since epoch

    /** Text‑only constructor */
    public Message(String text, boolean sentByUser, long timestamp) {
        this.id         = String.valueOf(timestamp);
        this.text       = text;
        this.imageUri   = null;
        this.sentByUser = sentByUser;
        this.timestamp  = timestamp;
    }

    /** Image‑only constructor */
    public Message(String imageUri, boolean sentByUser, long timestamp, boolean isImage) {
        this.id         = String.valueOf(timestamp);
        this.text       = null;
        this.imageUri   = imageUri;
        this.sentByUser = sentByUser;
        this.timestamp  = timestamp;
    }

    /** Unique identifier for this message */
    public String getId() {
        return id;
    }

    /** Get the text payload, or null if this is an image */
    public String getText() {
        return text;
    }

    /** Get the image URI payload, or null if this is text */
    public String getImageUri() {
        return imageUri;
    }

    /** True if this message carries an image rather than text */
    public boolean hasImage() {
        return imageUri != null;
    }

    /** True if the current user sent this message */
    public boolean isSentByUser() {
        return sentByUser;
    }

    /** Timestamp (ms since epoch) */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Convert to a Map for Firestore storage.
     * Fields that are null (text or imageUri) are still written as null.
     */
    public Map<String, Object> getAsMap() {
        Map<String, Object> m = new HashMap<>();
        m.put("id",        id);
        m.put("text",      text);
        m.put("imageUri",  imageUri);
        m.put("sentByUser", sentByUser);
        m.put("timestamp", timestamp);
        return m;
    }
}
