package com.forge.suggestions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Suggestion {
    @JsonProperty("title")
    private final String title;
    
    @JsonProperty("description")
    private final String description;
    
    @JsonProperty("location")
    private final String location;
    
    @JsonProperty("impact")
    private final String impact;
    
    @JsonProperty("beforeExample")
    private final String beforeExample;
    
    @JsonProperty("afterExample")
    private final String afterExample;
    
    public Suggestion(String title, String description, String location, String impact, 
                     String beforeExample, String afterExample) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.impact = impact;
        this.beforeExample = beforeExample;
        this.afterExample = afterExample;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public String getImpact() {
        return impact;
    }
    
    public String getBeforeExample() {
        return beforeExample;
    }
    
    public String getAfterExample() {
        return afterExample;
    }
}
