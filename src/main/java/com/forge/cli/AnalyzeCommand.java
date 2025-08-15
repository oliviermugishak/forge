package com.forge.cli;

import com.forge.analyzer.CodeAnalyzer;
import com.forge.analyzer.AnalysisResult;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

@CommandLine.Command(
    name = "analyze",
    description = "Analyze code for inefficient patterns"
)
public class AnalyzeCommand implements Callable<Integer> {
    
    @CommandLine.Parameters(index = "0", description = "Path to the code to analyze")
    private Path path;
    
    @CommandLine.Option(names = {"--lang", "-l"}, description = "Programming language", defaultValue = "java")
    private String language;
    
    @Override
    public Integer call() {
        try {
            CodeAnalyzer analyzer = new CodeAnalyzer();
            AnalysisResult result = analyzer.analyze(path, language);
            
            System.out.println("🔍 Analysis Results for " + path);
            System.out.println("Language: " + language);
            System.out.println("Files analyzed: " + result.getFilesAnalyzed());
            System.out.println("Issues found: " + result.getIssues().size());
            System.out.println();
            
            if (result.getIssues().isEmpty()) {
                System.out.println("✅ No inefficiencies detected!");
            } else {
                System.out.println("⚠️  Inefficiencies found:");
                result.getIssues().forEach(issue -> {
                    System.out.println("  • " + issue.getDescription());
                    System.out.println("    Location: " + issue.getLocation());
                    System.out.println("    Severity: " + issue.getSeverity());
                    System.out.println();
                });
            }
            
            return 0;
        } catch (Exception e) {
            System.err.println("❌ Error during analysis: " + e.getMessage());
            return 1;
        }
    }
}
