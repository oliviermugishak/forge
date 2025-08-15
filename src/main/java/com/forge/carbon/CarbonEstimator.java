package com.forge.carbon;

import com.forge.analyzer.CodeAnalyzer;
import com.forge.analyzer.AnalysisResult;
import com.forge.analyzer.Issue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CarbonEstimator {
    
    // Constants for carbon estimation
    private static final double AVG_CPU_POWER_WATTS = 65.0; // Average CPU power consumption
    private static final double CO2_PER_KWH = 0.5; // kg CO2 per kWh (global average)
    private static final double WATTS_TO_KWH = 1.0 / 1000.0; // Convert watts to kilowatts
    
    public EstimateResult estimate(Path path, String language) throws IOException {
        CodeAnalyzer analyzer = new CodeAnalyzer();
        AnalysisResult analysis = analyzer.analyze(path, language);
        
        // Calculate base CPU time based on code complexity
        double baseCpuTimeMs = calculateBaseCpuTime(path, analysis);
        
        // Calculate energy usage
        double energyWh = (baseCpuTimeMs / 1000.0) * AVG_CPU_POWER_WATTS * WATTS_TO_KWH * 3600; // Convert to Wh
        
        // Calculate CO2 emissions
        double co2Grams = energyWh * CO2_PER_KWH * 1000; // Convert kg to grams
        
        // Calculate potential savings based on issues found
        double potentialSavings = calculatePotentialSavings(analysis.getIssues());
        double potentialCo2Reduction = potentialSavings * CO2_PER_KWH * 1000;
        double savingsPercentage = (potentialSavings / energyWh) * 100;
        
        return new EstimateResult(
            baseCpuTimeMs,
            energyWh,
            co2Grams,
            potentialSavings,
            potentialCo2Reduction,
            savingsPercentage
        );
    }
    
    private double calculateBaseCpuTime(Path path, AnalysisResult analysis) throws IOException {
        double baseTime = 0.0;
        
        if (Files.isDirectory(path)) {
            // Estimate based on number of files and complexity
            long fileCount = Files.walk(path)
                .filter(p -> p.toString().endsWith(".java"))
                .count();
            baseTime = fileCount * 10.0; // 10ms per file as baseline
        } else {
            baseTime = 10.0; // 10ms for single file
        }
        
        // Add complexity penalty based on issues
        for (Issue issue : analysis.getIssues()) {
            switch (issue.getSeverity()) {
                case "HIGH":
                    baseTime *= 1.5; // 50% increase for high severity issues
                    break;
                case "MEDIUM":
                    baseTime *= 1.2; // 20% increase for medium severity issues
                    break;
                case "LOW":
                    baseTime *= 1.1; // 10% increase for low severity issues
                    break;
            }
        }
        
        return baseTime;
    }
    
    private double calculatePotentialSavings(List<Issue> issues) {
        double savings = 0.0;
        
        for (Issue issue : issues) {
            switch (issue.getDescription()) {
                case "Deep nested loops detected":
                    savings += 0.4; // 40% potential savings
                    break;
                case "String concatenation in loop":
                    savings += 0.2; // 20% potential savings
                    break;
                case "Repeated method call detected":
                    savings += 0.3; // 30% potential savings
                    break;
            }
        }
        
        return savings;
    }
}
