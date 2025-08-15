package com.forge.suggestions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class SuggestionResult {
    @JsonProperty("suggestions")
    private final List<Suggestion> suggestions;
    
    public SuggestionResult(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
    }
    
    public List<Suggestion> getSuggestions() {
        return suggestions;
    }
    
    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            return "{\"error\": \"Failed to serialize to JSON\"}";
        }
    }
}
