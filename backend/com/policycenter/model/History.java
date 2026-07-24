package com.policycenter.model;

public class History {
    private String publicID;
    private String eventTimestamp;
    private String username;
    private String description;
    private String eventType;

    public History() {}

    public History(String publicID, String username, String description, String eventType) {
        this.publicID = publicID;
        this.username = username;
        this.description = description;
        this.eventType = eventType;
        this.eventTimestamp = java.time.LocalDateTime.now().toString();
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getEventTimestamp() { return eventTimestamp; }
    public void setEventTimestamp(String eventTimestamp) { this.eventTimestamp = eventTimestamp; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
}
