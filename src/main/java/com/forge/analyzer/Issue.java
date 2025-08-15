package com.forge.analyzer;

public class Issue {
    private final String description;
    private final String location;
    private final String severity;
    private final String details;
    
    public Issue(String description, String location, String severity, String details) {
        this.description = description;
        this.location = location;
        this.severity = severity;
        this.details = details;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public String getDetails() {
        return details;
    }
    
    @Override
    public String toString() {
        return String.format("%s at %s (%s): %s", description, location, severity, details);
    }
}
