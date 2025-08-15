package com.forge.cli;

import com.forge.carbon.CarbonEstimator;
import com.forge.carbon.EstimateResult;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "estimate",
    description = "Estimate carbon footprint of code execution"
)
public class EstimateCommand implements Callable<Integer> {
    
    @CommandLine.Parameters(index = "0", description = "Path to the code to analyze")
    private Path path;
    
    @CommandLine.Option(names = {"--lang", "-l"}, description = "Programming language", defaultValue = "java")
    private String language;
    
    @CommandLine.Option(names = {"--output", "-o"}, description = "Output format", defaultValue = "text")
    private String outputFormat;
    
    @Override
    public Integer call() {
        try {
            CarbonEstimator estimator = new CarbonEstimator();
            EstimateResult result = estimator.estimate(path, language);
            
            if ("json".equalsIgnoreCase(outputFormat)) {
                System.out.println(result.toJson());
            } else {
                System.out.println("🌱 Carbon Footprint Estimate for " + path);
                System.out.println("Language: " + language);
                System.out.println();
                System.out.println("📊 Current Estimate:");
                System.out.println("  • CPU Time: " + String.format("%.2f", result.getEstimatedCpuTimeMs()) + " ms");
                System.out.println("  • Energy Usage: " + String.format("%.4f", result.getEstimatedEnergyWh()) + " Wh");
                System.out.println("  • CO₂ Emissions: " + String.format("%.6f", result.getEstimatedCo2Grams()) + " g CO₂");
                System.out.println();
                
                if (result.getPotentialSavings() > 0) {
                    System.out.println("💚 Potential Savings (with optimizations):");
                    System.out.println("  • Energy Savings: " + String.format("%.4f", result.getPotentialSavings()) + " Wh");
                    System.out.println("  • CO₂ Reduction: " + String.format("%.6f", result.getPotentialCo2Reduction()) + " g CO₂");
                    System.out.println("  • Percentage: " + String.format("%.1f", result.getSavingsPercentage()) + "%");
                }
            }
            
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Error during carbon estimation: " + e.getMessage());
            return 1;
        }
    }
}
