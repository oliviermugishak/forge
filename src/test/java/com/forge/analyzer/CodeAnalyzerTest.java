package com.forge.analyzer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CodeAnalyzerTest {
    
    private CodeAnalyzer analyzer;
    
    @BeforeEach
    void setUp() {
        analyzer = new CodeAnalyzer();
    }
    
    @Test
    void testAnalyzeEmptyDirectory(@TempDir Path tempDir) throws IOException {
        AnalysisResult result = analyzer.analyze(tempDir, "java");
        
        assertEquals(0, result.getFilesAnalyzed());
        assertTrue(result.getIssues().isEmpty());
    }
    
    @Test
    void testAnalyzeJavaFileWithNestedLoops(@TempDir Path tempDir) throws IOException {
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
        
        AnalysisResult result = analyzer.analyze(javaFile, "java");
        
        assertEquals(1, result.getFilesAnalyzed());
        assertFalse(result.getIssues().isEmpty());
        
        boolean foundNestedLoopIssue = result.getIssues().stream()
            .anyMatch(issue -> issue.getDescription().contains("Deep nested loops"));
        assertTrue(foundNestedLoopIssue);
    }
    
    @Test
    void testAnalyzeJavaFileWithStringConcatenation(@TempDir Path tempDir) throws IOException {
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
        
        AnalysisResult result = analyzer.analyze(javaFile, "java");
        
        assertEquals(1, result.getFilesAnalyzed());
        assertFalse(result.getIssues().isEmpty());
        
        boolean foundStringConcatenationIssue = result.getIssues().stream()
            .anyMatch(issue -> issue.getDescription().contains("String concatenation"));
        assertTrue(foundStringConcatenationIssue);
    }
    
    @Test
    void testAnalyzeJavaFileWithRepeatedMethodCalls(@TempDir Path tempDir) throws IOException {
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
        
        AnalysisResult result = analyzer.analyze(javaFile, "java");
        
        assertEquals(1, result.getFilesAnalyzed());
        assertFalse(result.getIssues().isEmpty());
        
        boolean foundRepeatedMethodCallIssue = result.getIssues().stream()
            .anyMatch(issue -> issue.getDescription().contains("Repeated method call"));
        assertTrue(foundRepeatedMethodCallIssue);
    }
    
    @Test
    void testAnalyzeCleanJavaFile(@TempDir Path tempDir) throws IOException {
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
        
        AnalysisResult result = analyzer.analyze(javaFile, "java");
        
        assertEquals(1, result.getFilesAnalyzed());
        assertTrue(result.getIssues().isEmpty());
    }
    
    @Test
    void testAnalyzeMultipleJavaFiles(@TempDir Path tempDir) throws IOException {
        String code1 = """
            public class TestClass1 {
                public void method() {
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
        
        String code2 = """
            public class TestClass2 {
                public void method() {
                    // Clean code
                }
            }
            """;
        
        Path file1 = tempDir.resolve("TestClass1.java");
        Path file2 = tempDir.resolve("TestClass2.java");
        
        Files.writeString(file1, code1);
        Files.writeString(file2, code2);
        
        AnalysisResult result = analyzer.analyze(tempDir, "java");
        
        assertEquals(2, result.getFilesAnalyzed());
        assertFalse(result.getIssues().isEmpty());
    }
}
