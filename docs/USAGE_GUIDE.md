# Forge Usage Guide 🔥

## Table of Contents
1. [What is Forge?](#what-is-forge)
2. [How It Works](#how-it-works)
3. [Installation](#installation)
4. [Quick Start](#quick-start)
5. [Command Reference](#command-reference)
6. [Examples](#examples)
7. [Understanding Results](#understanding-results)
8. [Troubleshooting](#troubleshooting)
9. [Best Practices](#best-practices)
10. [Limitations](#limitations)

## What is Forge?

Forge is a **static code analysis tool** that helps developers write more energy-efficient code. It analyzes your codebase, detects inefficient patterns, and suggests greener alternatives.

### Key Features
- 🔍 **Pattern Detection**: Identifies common inefficient coding patterns
- 💡 **Smart Suggestions**: Provides specific optimization recommendations
- 🌱 **Carbon Estimation**: Calculates potential energy savings and CO₂ reduction
- 📊 **Multiple Formats**: Output in human-readable text or machine-readable JSON
- ⚡ **Fast Analysis**: Built for speed with minimal overhead

## How It Works

### Detection Method
Forge uses **rule-based static analysis**:

1. **Parsing**: Converts your code into an Abstract Syntax Tree (AST)
2. **Pattern Matching**: Applies predefined rules to detect inefficiencies
3. **Analysis**: Evaluates code complexity and potential impact
4. **Reporting**: Generates detailed reports with suggestions

### Current Detection Rules

#### High Severity Issues
- **Deep Nested Loops**: Loops with nesting depth > 2
  ```java
  // ❌ Inefficient - O(n³) complexity
  for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
          for (int k = 0; k < n; k++) {
              // Processing
          }
      }
  }
  ```

#### Medium Severity Issues
- **String Concatenation in Loops**: Using `+` operator in loops
  ```java
  // ❌ Inefficient - creates many String objects
  String result = "";
  for (int i = 0; i < 1000; i++) {
      result += "item" + i;
  }
  
  // ✅ Efficient - uses StringBuilder
  StringBuilder result = new StringBuilder();
  for (int i = 0; i < 1000; i++) {
      result.append("item").append(i);
  }
  ```

- **Repeated Method Calls**: Calling expensive methods multiple times
  ```java
  // ❌ Inefficient - calls expensiveCalculation() twice
  for (int i = 0; i < 100; i++) {
      process(expensiveCalculation());
      validate(expensiveCalculation());
  }
  
  // ✅ Efficient - calls once and reuses result
  for (int i = 0; i < 100; i++) {
      String result = expensiveCalculation();
      process(result);
      validate(result);
  }
  ```

## Installation

### Prerequisites
- Java 21 or higher
- Maven 3.6+ (for building from source)

### Build from Source
```bash
git clone <repository-url>
cd forge
mvn clean package
```

The executable JAR will be created at `target/forge-1.0.0.jar`.

## Quick Start

### 1. Analyze Your Code
```bash
java -jar forge-1.0.0.jar analyze ./myproject --lang java
```

### 2. Get Optimization Suggestions
```bash
java -jar forge-1.0.0.jar suggest ./myproject --lang java
```

### 3. Estimate Carbon Footprint
```bash
java -jar forge-1.0.0.jar estimate ./myproject --lang java
```

## Command Reference

### Analyze Command
```bash
forge analyze <path> [--lang java|python|javascript]
```

**Purpose**: Detects inefficient patterns in your code

**Parameters**:
- `<path>`: Path to file or directory to analyze
- `--lang`: Programming language (currently only Java supported)

**Output**: List of detected issues with severity levels

### Suggest Command
```bash
forge suggest <path> [--lang java|python|javascript] [--output text|json]
```

**Purpose**: Provides specific optimization recommendations

**Parameters**:
- `<path>`: Path to file or directory to analyze
- `--lang`: Programming language
- `--output`: Output format (text or JSON)

**Output**: Detailed suggestions with before/after examples

### Estimate Command
```bash
forge estimate <path> [--lang java|python|javascript] [--output text|json]
```

**Purpose**: Calculates carbon footprint and potential savings

**Parameters**:
- `<path>`: Path to file or directory to analyze
- `--lang`: Programming language
- `--output`: Output format (text or JSON)

**Output**: Energy usage, CO₂ emissions, and potential savings

## Examples

### Example 1: Analyzing a Single File
```bash
# Analyze a Java file
java -jar forge-1.0.0.jar analyze ./src/main/java/MyClass.java --lang java
```

**Output**:
```
🔍 Analysis Results for ./src/main/java/MyClass.java
Language: java
Files analyzed: 1
Issues found: 2

⚠️  Inefficiencies found:
  • String concatenation in loop
    Location: ./src/main/java/MyClass.java:15
    Severity: MEDIUM

  • Repeated method call detected
    Location: ./src/main/java/MyClass.java:23
    Severity: MEDIUM
```

### Example 2: Getting Optimization Suggestions
```bash
java -jar forge-1.0.0.jar suggest ./src/main/java/MyClass.java --lang java
```

**Output**:
```
💡 Optimization Suggestions for ./src/main/java/MyClass.java
Language: java
Suggestions found: 2

🔧 Use StringBuilder for string concatenation
   Description: Replace string concatenation with StringBuilder to avoid creating multiple string objects
   Location: ./src/main/java/MyClass.java:15
   Impact: MEDIUM
   Example:
     Before: String result = \"\";
for (int i = 0; i < n; i++) {
  result += \"item\" + i;\n}
     After:  StringBuilder result = new StringBuilder();
for (int i = 0; i < n; i++) {
  result.append(\"item\").append(i);\n}
String finalResult = result.toString();
```

### Example 3: Carbon Footprint Estimation
```bash
java -jar forge-1.0.0.jar estimate ./src/main/java/MyClass.java --lang java
```

**Output**:
```
🌱 Carbon Footprint Estimate for ./src/main/java/MyClass.java
Language: java

📊 Current Estimate:
  • CPU Time: 45.20 ms
  • Energy Usage: 10.5840 Wh
  • CO₂ Emissions: 5292.000000 g CO₂

💚 Potential Savings (with optimizations):
  • Energy Savings: 2.1000 Wh
  • CO₂ Reduction: 1050.000000 g CO₂
  • Percentage: 19.8%
```

### Example 4: JSON Output
```bash
java -jar forge-1.0.0.jar suggest ./src/main/java/MyClass.java --lang java --output json
```

**Output**:
```json
{
  "suggestions": [
    {
      "title": "Use StringBuilder for string concatenation",
      "description": "Replace string concatenation with StringBuilder to avoid creating multiple string objects",
      "location": "./src/main/java/MyClass.java:15",
      "impact": "MEDIUM",
      "beforeExample": "String result = \"\";\nfor (int i = 0; i < n; i++) {\n  result += \"item\" + i;\n}",
      "afterExample": "StringBuilder result = new StringBuilder();\nfor (int i = 0; i < n; i++) {\n  result.append(\"item\").append(i);\n}\nString finalResult = result.toString();"
    }
  ]
}
```

## Understanding Results

### Severity Levels
- **HIGH**: Critical issues that can cause significant performance problems
- **MEDIUM**: Issues that impact performance but are less critical
- **LOW**: Minor optimizations with limited impact

### Carbon Estimation Model
Forge uses a simplified model:
```
Estimated CPU Time (ms) × Avg CPU Power (65W) × CO₂/kWh (0.5 kg)
```

**Factors Considered**:
- Code complexity (number of files, methods, loops)
- Detected inefficiencies (severity-based penalties)
- Estimated execution time

### Understanding Savings
- **Energy Savings**: Potential reduction in energy consumption
- **CO₂ Reduction**: Corresponding reduction in carbon emissions
- **Percentage**: Percentage improvement with optimizations

## Troubleshooting

### Common Issues

#### 1. "Error during analysis" Message
**Cause**: File parsing error or unsupported language
**Solution**: 
- Ensure the file is valid Java code
- Check file permissions
- Verify the file path is correct

#### 2. No Issues Detected
**Cause**: Code is already optimized or doesn't match detection patterns
**Solution**:
- Check if your code contains the patterns Forge looks for
- Try with a file that has known inefficiencies

#### 3. Build Errors
**Cause**: Missing dependencies or Java version issues
**Solution**:
- Ensure Java 21+ is installed: `java -version`
- Check Maven installation: `mvn -version`
- Clean and rebuild: `mvn clean package`

### Debug Mode
For more detailed error information, you can run with verbose logging:
```bash
java -jar forge-1.0.0.jar analyze ./myfile.java --lang java 2>&1 | tee debug.log
```

## Best Practices

### 1. Regular Analysis
- Run Forge regularly during development
- Integrate into your CI/CD pipeline
- Review results before code reviews

### 2. Prioritize Fixes
- Start with HIGH severity issues
- Focus on frequently executed code paths
- Consider the impact vs. effort ratio

### 3. Team Integration
- Share results with your team
- Use JSON output for automated processing
- Track improvements over time

### 4. Code Review Integration
```bash
# Analyze changed files in a PR
java -jar forge-1.0.0.jar analyze ./changed-files/ --lang java --output json > analysis.json
```

## Limitations

### Current Limitations
1. **Language Support**: Only Java is fully supported
2. **Detection Scope**: Limited to specific patterns
3. **Accuracy**: Estimates are approximations
4. **Context Awareness**: Doesn't understand business logic

### What Forge Doesn't Do
- ❌ **Runtime Analysis**: Only static analysis
- ❌ **Database Optimization**: No SQL analysis
- ❌ **Memory Profiling**: No memory usage analysis
- ❌ **Network Optimization**: No I/O analysis
- ❌ **Algorithm Suggestions**: No AI-powered recommendations

### Future Enhancements
- [ ] Python and JavaScript support
- [ ] More sophisticated detection rules
- [ ] Machine learning-based analysis
- [ ] IDE plugin integration
- [ ] Database query optimization
- [ ] Memory usage analysis

## Getting Help

### Documentation
- [Project README](./README.md)
- [Project Plan](./project-plan.md)

### Common Patterns to Avoid

#### 1. Nested Loops
```java
// ❌ Avoid deep nesting
for (int i = 0; i < n; i++) {
    for (int j = 0; j < n; j++) {
        for (int k = 0; k < n; k++) {
            // O(n³) complexity
        }
    }
}

// ✅ Use more efficient algorithms
// Consider: divide-and-conquer, dynamic programming, or data structure changes
```

#### 2. String Operations
```java
// ❌ Inefficient string building
String result = "";
for (int i = 0; i < 1000; i++) {
    result += "item" + i;
}

// ✅ Use StringBuilder
StringBuilder result = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    result.append("item").append(i);
}
String finalResult = result.toString();
```

#### 3. Repeated Computations
```java
// ❌ Expensive repeated calls
for (int i = 0; i < 100; i++) {
    process(expensiveCalculation());
    validate(expensiveCalculation());
}

// ✅ Cache results
for (int i = 0; i < 100; i++) {
    String result = expensiveCalculation();
    process(result);
    validate(result);
}
```

---

**Happy coding sustainably! 🌱**
