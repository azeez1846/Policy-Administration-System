package com.policycenter.model;

public class Note {
    private String publicID;
    private String subject;
    private String body;
    private String author;
    private String topic;
    private String securityLevel; // Public, Sensitive, Internal

    public Note() {}

    public Note(String publicID, String subject, String body, String author, String topic) {
        this.publicID = publicID;
        this.subject = subject;
        this.body = body;
        this.author = author;
        this.topic = topic;
        this.securityLevel = "Public";
    }

    public String getPublicID() { return publicID; }
    public void setPublicID(String publicID) { this.publicID = publicID; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getSecurityLevel() { return securityLevel; }
    public void setSecurityLevel(String securityLevel) { this.securityLevel = securityLevel; }
}
