package com.forge.cli;

import com.forge.suggestions.OptimizationSuggester;
import com.forge.suggestions.SuggestionResult;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "suggest",
    description = "Get optimization suggestions for code"
)
public class SuggestCommand implements Callable<Integer> {
    
    @CommandLine.Parameters(index = "0", description = "Path to the code to analyze")
    private Path path;
    
    @CommandLine.Option(names = {"--lang", "-l"}, description = "Programming language", defaultValue = "java")
    private String language;
    
    @CommandLine.Option(names = {"--output", "-o"}, description = "Output format", defaultValue = "text")
    private String outputFormat;
    
    @Override
    public Integer call() {
        try {
            OptimizationSuggester suggester = new OptimizationSuggester();
            SuggestionResult result = suggester.suggest(path, language);
            
            if ("json".equalsIgnoreCase(outputFormat)) {
                System.out.println(result.toJson());
            } else {
                System.out.println("💡 Optimization Suggestions for " + path);
                System.out.println("Language: " + language);
                System.out.println("Suggestions found: " + result.getSuggestions().size());
                System.out.println();
                
                if (result.getSuggestions().isEmpty()) {
                    System.out.println("✅ No optimization suggestions available!");
                } else {
                    result.getSuggestions().forEach(suggestion -> {
                        System.out.println("🔧 " + suggestion.getTitle());
                        System.out.println("   Description: " + suggestion.getDescription());
                        System.out.println("   Location: " + suggestion.getLocation());
                        System.out.println("   Impact: " + suggestion.getImpact());
                        System.out.println("   Example:");
                        System.out.println("     Before: " + suggestion.getBeforeExample());
                        System.out.println("     After:  " + suggestion.getAfterExample());
                        System.out.println();
                    });
                }
            }
            
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Error during suggestion generation: " + e.getMessage());
            return 1;
        }
    }
}
