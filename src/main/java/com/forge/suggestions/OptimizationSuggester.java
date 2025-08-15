package com.forge.suggestions;

import com.forge.analyzer.CodeAnalyzer;
import com.forge.analyzer.AnalysisResult;
import com.forge.analyzer.Issue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OptimizationSuggester {
    
    public SuggestionResult suggest(Path path, String language) throws IOException {
        CodeAnalyzer analyzer = new CodeAnalyzer();
        AnalysisResult analysis = analyzer.analyze(path, language);
        
        List<Suggestion> suggestions = new ArrayList<>();
        
        for (Issue issue : analysis.getIssues()) {
            suggestions.addAll(generateSuggestionsForIssue(issue));
        }
        
        return new SuggestionResult(suggestions);
    }
    
    private List<Suggestion> generateSuggestionsForIssue(Issue issue) {
        List<Suggestion> suggestions = new ArrayList<>();
        
        switch (issue.getDescription()) {
            case "Deep nested loops detected":
                suggestions.add(new Suggestion(
                    "Use divide-and-conquer algorithm",
                    "Replace nested loops with a more efficient algorithm like divide-and-conquer",
                    issue.getLocation(),
                    "HIGH",
                    "for (int i = 0; i < n; i++) {\n  for (int j = 0; j < n; j++) {\n    for (int k = 0; k < n; k++) {\n      // O(n³) complexity\n    }\n  }\n}",
                    "// Use divide-and-conquer or dynamic programming\n// Example: Merge sort, Quick sort, or matrix multiplication algorithms"
                ));
                break;
                
            case "String concatenation in loop":
                suggestions.add(new Suggestion(
                    "Use StringBuilder for string concatenation",
                    "Replace string concatenation with StringBuilder to avoid creating multiple string objects",
                    issue.getLocation(),
                    "MEDIUM",
                    "String result = \"\";\nfor (int i = 0; i < n; i++) {\n  result += \"item\" + i;\n}",
                    "StringBuilder result = new StringBuilder();\nfor (int i = 0; i < n; i++) {\n  result.append(\"item\").append(i);\n}\nString finalResult = result.toString();"
                ));
                break;
                
            case "Repeated method call detected":
                suggestions.add(new Suggestion(
                    "Cache method call results",
                    "Store the result of expensive method calls in a variable to avoid repeated computation",
                    issue.getLocation(),
                    "MEDIUM",
                    "for (int i = 0; i < n; i++) {\n  process(expensiveCalculation());\n  validate(expensiveCalculation());\n}",
                    "for (int i = 0; i < n; i++) {\n  String result = expensiveCalculation();\n  process(result);\n  validate(result);\n}"
                ));
                break;
        }
        
        return suggestions;
    }
}
