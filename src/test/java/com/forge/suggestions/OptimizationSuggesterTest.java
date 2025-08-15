package com.forge.suggestions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptimizationSuggesterTest {
    
    private OptimizationSuggester suggester;
    
    @BeforeEach
    void setUp() {
        suggester = new OptimizationSuggester();
    }
    
    @Test
    void testSuggestForCleanCode(@TempDir Path tempDir) throws IOException {
        String code = """
            public class TestClass {
                public void efficientMethod() {
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Clean code");
                    }
                }
            }
            """;
        
        Path javaFile = tempDir.resolve("TestClass.java");
        Files.writeString(javaFile, code);
        
        SuggestionResult result = suggester.suggest(javaFile, "java");
        
        assertTrue(result.getSuggestions().isEmpty());
    }
    
    @Test
    void testSuggestForNestedLoops(@TempDir Path tempDir) throws IOException {
        String code = """
            public class TestClass {
                public void inefficientMethod() {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            for (int k = 0; k < 10; k++) {
                                System.out.println("Nested loop");
                            }
                        }
                    }
                }
            }
            """;
        
        Path javaFile = tempDir.resolve("TestClass.java");
        Files.writeString(javaFile, code);
        
        SuggestionResult result = suggester.suggest(javaFile, "java");
        
        assertFalse(result.getSuggestions().isEmpty());
        
        boolean foundDivideAndConquerSuggestion = result.getSuggestions().stream()
            .anyMatch(suggestion -> suggestion.getTitle().contains("divide-and-conquer"));
        assertTrue(foundDivideAndConquerSuggestion);
    }
    
    @Test
    void testSuggestForStringConcatenation(@TempDir Path tempDir) throws IOException {
        String code = """
            public class TestClass {
                public void inefficientMethod() {
                    String result = "";
                    for (int i = 0; i < 10; i++) {
                        result += "item" + i;
                    }
                }
            }
            """;
        
        Path javaFile = tempDir.resolve("TestClass.java");
        Files.writeString(javaFile, code);
        
        SuggestionResult result = suggester.suggest(javaFile, "java");
        
        assertFalse(result.getSuggestions().isEmpty());
        
        boolean foundStringBuilderSuggestion = result.getSuggestions().stream()
            .anyMatch(suggestion -> suggestion.getTitle().contains("StringBuilder"));
        assertTrue(foundStringBuilderSuggestion);
    }
    
    @Test
    void testSuggestForRepeatedMethodCalls(@TempDir Path tempDir) throws IOException {
        String code = """
            public class TestClass {
                public void inefficientMethod() {
                    for (int i = 0; i < 10; i++) {
                        process(expensiveCalculation());
                        validate(expensiveCalculation());
                    }
                }
                
                private String expensiveCalculation() {
                    return "expensive";
                }
                
                private void process(String input) {}
                private void validate(String input) {}
            }
            """;
        
        Path javaFile = tempDir.resolve("TestClass.java");
        Files.writeString(javaFile, code);
        
        SuggestionResult result = suggester.suggest(javaFile, "java");
        
        assertFalse(result.getSuggestions().isEmpty());
        
        boolean foundCachingSuggestion = result.getSuggestions().stream()
            .anyMatch(suggestion -> suggestion.getTitle().contains("Cache"));
        assertTrue(foundCachingSuggestion);
    }
    
    @Test
    void testSuggestJsonOutput(@TempDir Path tempDir) throws IOException {
        String code = """
            public class TestClass {
                public void inefficientMethod() {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 10; j++) {
                            for (int k = 0; k < 10; k++) {
                                // Nested loops
                            }
                        }
                    }
                }
            }
            """;
        
        Path javaFile = tempDir.resolve("TestClass.java");
        Files.writeString(javaFile, code);
        
        SuggestionResult result = suggester.suggest(javaFile, "java");
        String json = result.toJson();
        
        assertNotNull(json);
        assertTrue(json.contains("suggestions"));
        assertFalse(json.contains("error"));
    }
}
