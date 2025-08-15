package com.forge.carbon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CarbonEstimatorTest {
    
    private CarbonEstimator estimator;
    
    @BeforeEach
    void setUp() {
        estimator = new CarbonEstimator();
    }
    
    @Test
    void testEstimateCleanCode(@TempDir Path tempDir) throws IOException {
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
        
        EstimateResult result = estimator.estimate(javaFile, "java");
        
        assertTrue(result.getEstimatedCpuTimeMs() > 0);
        assertTrue(result.getEstimatedEnergyWh() > 0);
        assertTrue(result.getEstimatedCo2Grams() > 0);
        assertEquals(0.0, result.getPotentialSavings(), 0.001);
        assertEquals(0.0, result.getPotentialCo2Reduction(), 0.001);
        assertEquals(0.0, result.getSavingsPercentage(), 0.001);
    }
    
    @Test
    void testEstimateWithNestedLoops(@TempDir Path tempDir) throws IOException {
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
        
        EstimateResult result = estimator.estimate(javaFile, "java");
        
        assertTrue(result.getEstimatedCpuTimeMs() > 0);
        assertTrue(result.getEstimatedEnergyWh() > 0);
        assertTrue(result.getEstimatedCo2Grams() > 0);
        assertTrue(result.getPotentialSavings() > 0);
        assertTrue(result.getPotentialCo2Reduction() > 0);
        assertTrue(result.getSavingsPercentage() > 0);
    }
    
    @Test
    void testEstimateWithStringConcatenation(@TempDir Path tempDir) throws IOException {
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
        
        EstimateResult result = estimator.estimate(javaFile, "java");
        
        assertTrue(result.getEstimatedCpuTimeMs() > 0);
        assertTrue(result.getEstimatedEnergyWh() > 0);
        assertTrue(result.getEstimatedCo2Grams() > 0);
        assertTrue(result.getPotentialSavings() > 0);
        assertTrue(result.getPotentialCo2Reduction() > 0);
        assertTrue(result.getSavingsPercentage() > 0);
    }
    
    @Test
    void testEstimateMultipleFiles(@TempDir Path tempDir) throws IOException {
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
        
        EstimateResult result = estimator.estimate(tempDir, "java");
        
        assertTrue(result.getEstimatedCpuTimeMs() > 0);
        assertTrue(result.getEstimatedEnergyWh() > 0);
        assertTrue(result.getEstimatedCo2Grams() > 0);
        assertTrue(result.getPotentialSavings() > 0);
        assertTrue(result.getPotentialCo2Reduction() > 0);
        assertTrue(result.getSavingsPercentage() > 0);
    }
    
    @Test
    void testEstimateJsonOutput(@TempDir Path tempDir) throws IOException {
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
        
        EstimateResult result = estimator.estimate(javaFile, "java");
        String json = result.toJson();
        
        assertNotNull(json);
        assertTrue(json.contains("estimatedCpuTimeMs"));
        assertTrue(json.contains("estimatedEnergyWh"));
        assertTrue(json.contains("estimatedCo2Grams"));
        assertFalse(json.contains("error"));
    }
}
