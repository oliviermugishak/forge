package com.forge.carbon;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EstimateResult {
    @JsonProperty("estimatedCpuTimeMs")
    private final double estimatedCpuTimeMs;
    
    @JsonProperty("estimatedEnergyWh")
    private final double estimatedEnergyWh;
    
    @JsonProperty("estimatedCo2Grams")
    private final double estimatedCo2Grams;
    
    @JsonProperty("potentialSavings")
    private final double potentialSavings;
    
    @JsonProperty("potentialCo2Reduction")
    private final double potentialCo2Reduction;
    
    @JsonProperty("savingsPercentage")
    private final double savingsPercentage;
    
    public EstimateResult(double estimatedCpuTimeMs, double estimatedEnergyWh, double estimatedCo2Grams,
                         double potentialSavings, double potentialCo2Reduction, double savingsPercentage) {
        this.estimatedCpuTimeMs = estimatedCpuTimeMs;
        this.estimatedEnergyWh = estimatedEnergyWh;
        this.estimatedCo2Grams = estimatedCo2Grams;
        this.potentialSavings = potentialSavings;
        this.potentialCo2Reduction = potentialCo2Reduction;
        this.savingsPercentage = savingsPercentage;
    }
    
    public double getEstimatedCpuTimeMs() {
        return estimatedCpuTimeMs;
    }
    
    public double getEstimatedEnergyWh() {
        return estimatedEnergyWh;
    }
    
    public double getEstimatedCo2Grams() {
        return estimatedCo2Grams;
    }
    
    public double getPotentialSavings() {
        return potentialSavings;
    }
    
    public double getPotentialCo2Reduction() {
        return potentialCo2Reduction;
    }
    
    public double getSavingsPercentage() {
        return savingsPercentage;
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
