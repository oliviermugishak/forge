# Forge Developer Guide 🔧

## Table of Contents
1. [System Architecture](#system-architecture)
2. [Core Components](#core-components)
3. [Design Patterns](#design-patterns)
4. [Implementation Details](#implementation-details)
5. [Extension Points](#extension-points)
6. [Adding New Features](#adding-new-features)
7. [Testing Strategy](#testing-strategy)
8. [Performance Considerations](#performance-considerations)

## System Architecture

### Overview
Forge follows a **modular, plugin-based architecture** designed for extensibility. The system is built around the principle of separation of concerns with clear interfaces between components.

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   CLI Layer     │    │  Analysis Layer │    │  Output Layer   │
│                 │    │                 │    │                 │
│ • Commands      │───▶│ • Code Analyzer │───▶│ • Formatters    │
│ • Arguments     │    │ • Rule Engine   │    │ • JSON/Text     │
│ • Help System   │    │ • AST Visitors  │    │ • Reports       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  Carbon Layer   │    │ Suggestions     │    │  Utilities      │
│                 │    │                 │    │                 │
│ • Estimator     │    │ • Suggester     │    │ • File Utils    │
│ • Models        │    │ • Templates     │    │ • Validation    │
│ • Calculations  │    │ • Examples      │    │ • Error Handling│
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Key Design Principles

1. **Single Responsibility**: Each class has one clear purpose
2. **Open/Closed**: Open for extension, closed for modification
3. **Dependency Inversion**: High-level modules don't depend on low-level modules
4. **Interface Segregation**: Clients depend only on interfaces they use
5. **Composition over Inheritance**: Prefer composition for flexibility

## Core Components

### 1. CLI Layer (`com.forge.cli`)

#### Command Structure
```java
@CommandLine.Command(name = "analyze", description = "Analyze code for inefficient patterns")
public class AnalyzeCommand implements Callable<Integer> {
    @CommandLine.Parameters(index = "0", description = "Path to the code to analyze")
    private Path path;
    
    @CommandLine.Option(names = {"--lang", "-l"}, description = "Programming language")
    private String language;
}
```

**Why this design?**
- **Picocli Framework**: Provides robust CLI parsing with automatic help generation
- **Callable<Integer>**: Standard Java pattern for commands that return exit codes
- **Annotations**: Declarative configuration reduces boilerplate
- **Path Objects**: Type-safe file path handling

#### Main Entry Point
```java
@CommandLine.Command(name = "forge", subcommands = {AnalyzeCommand.class, ...})
public class ForgeMain {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new ForgeMain()).execute(args);
        System.exit(exitCode);
    }
}
```

**Design Decisions:**
- **Subcommand Pattern**: Allows for multiple related commands under one tool
- **Exit Code Handling**: Standard Unix practice for CLI tools
- **Automatic Help**: Picocli generates help text from annotations

### 2. Analysis Layer (`com.forge.analyzer`)

#### Code Analyzer
```java
public class CodeAnalyzer {
    public AnalysisResult analyze(Path path, String language) throws IOException {
        // 1. Determine if path is file or directory
        // 2. Find all relevant files
        // 3. Parse each file
        // 4. Apply analysis rules
        // 5. Aggregate results
    }
}
```

**Implementation Strategy:**
- **Language Agnostic**: The analyzer interface doesn't depend on specific languages
- **Batch Processing**: Processes multiple files efficiently
- **Error Resilience**: Continues analysis even if individual files fail

#### AST Visitor Pattern
```java
private static class InefficiencyVisitor extends VoidVisitorAdapter<List<Issue>> {
    @Override
    public void visit(MethodDeclaration md, List<Issue> issues) {
        super.visit(md, issues);
        checkNestedLoops(md, issues);
        checkStringConcatenationInLoops(md, issues);
        checkRepeatedMethodCalls(md, issues);
    }
}
```

**Why Visitor Pattern?**
- **Separation of Concerns**: Each check is isolated
- **Extensibility**: Easy to add new checks
- **Performance**: Single traversal of AST
- **Type Safety**: Compile-time checking of node types

#### Issue Detection Rules

##### Nested Loops Detection
```java
private void checkNestedLoops(MethodDeclaration md, List<Issue> issues) {
    md.findAll(ForStmt.class).forEach(forStmt -> {
        int nestingLevel = getNestingLevel(forStmt);
        if (nestingLevel > 2) {
            issues.add(new Issue("Deep nested loops detected", ...));
        }
    });
}

private int getNestingLevel(Statement stmt) {
    int level = 0;
    Statement current = stmt;
    while (current.getParentNode().isPresent()) {
        Node parentNode = current.getParentNode().get();
        if (parentNode instanceof ForStmt || parentNode instanceof WhileStmt) {
            level++;
            current = (Statement) parentNode;
        } else {
            break;
        }
    }
    return level;
}
```

**Algorithm Explanation:**
1. **Find All Loops**: Uses JavaParser's `findAll()` method
2. **Calculate Nesting**: Traverses up the AST tree counting loop nodes
3. **Threshold Check**: Triggers on depth > 2 (configurable)
4. **Type Safety**: Uses instanceof checks for different loop types

##### String Concatenation Detection
```java
private void checkStringConcatenationInLoops(MethodDeclaration md, List<Issue> issues) {
    md.findAll(ForStmt.class).forEach(forStmt -> {
        forStmt.findAll(StringLiteralExpr.class).forEach(str -> {
            if (str.getParentNode().isPresent() && 
                str.getParentNode().get().toString().contains("+")) {
                issues.add(new Issue("String concatenation in loop", ...));
            }
        });
    });
}
```

**Detection Logic:**
- **Context Awareness**: Only checks inside loops
- **Pattern Matching**: Looks for `+` operator in parent nodes
- **String Literals**: Focuses on string operations specifically

##### Repeated Method Calls Detection
```java
private void checkRepeatedMethodCalls(MethodDeclaration md, List<Issue> issues) {
    List<MethodCallExpr> methodCalls = md.findAll(MethodCallExpr.class);
    for (int i = 0; i < methodCalls.size(); i++) {
        for (int j = i + 1; j < methodCalls.size(); j++) {
            MethodCallExpr call1 = methodCalls.get(i);
            MethodCallExpr call2 = methodCalls.get(j);
            
            if (call1.getNameAsString().equals(call2.getNameAsString()) &&
                call1.getArguments().size() == call2.getArguments().size()) {
                issues.add(new Issue("Repeated method call detected", ...));
            }
        }
    }
}
```

**Algorithm Complexity:**
- **O(n²)**: Compares each method call with every other
- **Heuristic**: Assumes same method name + same argument count = same call
- **Limitation**: Doesn't analyze argument values (future enhancement)

### 3. Suggestions Layer (`com.forge.suggestions`)

#### Optimization Suggester
```java
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
}
```

**Design Pattern:**
- **Decorator Pattern**: Enhances analysis results with suggestions
- **Rule-Based Mapping**: Each issue type maps to specific suggestions
- **Composition**: Builds suggestions from multiple sources

### 4. Carbon Estimation Layer (`com.forge.carbon`)

#### Estimation Model
```java
public class CarbonEstimator {
    private static final double AVG_CPU_POWER_WATTS = 65.0;
    private static final double CO2_PER_KWH = 0.5;
    private static final double WATTS_TO_KWH = 1.0 / 1000.0;
    
    public EstimateResult estimate(Path path, String language) throws IOException {
        CodeAnalyzer analyzer = new CodeAnalyzer();
        AnalysisResult analysis = analyzer.analyze(path, language);
        
        double baseCpuTimeMs = calculateBaseCpuTime(path, analysis);
        double energyWh = (baseCpuTimeMs / 1000.0) * AVG_CPU_POWER_WATTS * WATTS_TO_KWH * 3600;
        double co2Grams = energyWh * CO2_PER_KWH * 1000;
        
        return new EstimateResult(baseCpuTimeMs, energyWh, co2Grams, ...);
    }
}
```

**Estimation Formula:**
```
CPU Time (ms) × CPU Power (65W) × Conversion Factors × CO₂/kWh (0.5 kg)
```

**Model Assumptions:**
- **Average CPU Power**: 65W (typical for modern processors)
- **Global CO₂ Average**: 0.5 kg CO₂ per kWh
- **Linear Scaling**: Assumes linear relationship between code complexity and execution time

## Design Patterns

### 1. Visitor Pattern
**Used in**: AST traversal for code analysis
**Benefits**: 
- Separates traversal logic from analysis logic
- Easy to add new analysis rules
- Type-safe node handling

### 2. Command Pattern
**Used in**: CLI command structure
**Benefits**:
- Consistent interface for all commands
- Easy to add new commands
- Standardized error handling

### 3. Strategy Pattern
**Used in**: Language-specific parsers (future)
**Benefits**:
- Pluggable language support
- Runtime language selection
- Consistent analysis interface

## Extension Points

### 1. Adding New Detection Rules

#### Step 1: Create Rule Class
```java
public class NewInefficiencyRule {
    public static void checkForNewPattern(MethodDeclaration md, List<Issue> issues) {
        // Your detection logic here
        md.findAll(SomeNodeType.class).forEach(node -> {
            if (isInefficient(node)) {
                issues.add(new Issue(
                    "New inefficiency detected",
                    getLocation(node),
                    "MEDIUM",
                    "Description of the issue"
                ));
            }
        });
    }
}
```

#### Step 2: Integrate with Visitor
```java
@Override
public void visit(MethodDeclaration md, List<Issue> issues) {
    super.visit(md, issues);
    // Existing checks...
    NewInefficiencyRule.checkForNewPattern(md, issues);
}
```

### 2. Adding New Languages

#### Step 1: Create Language Parser
```java
public class PythonParser implements LanguageParser {
    @Override
    public List<Issue> analyze(Path file) throws IOException {
        // Parse Python AST
        // Apply detection rules
        // Return issues
    }
}
```

#### Step 2: Register Parser
```java
public class CodeAnalyzer {
    private static final Map<String, LanguageParser> parsers = Map.of(
        "java", new JavaParser(),
        "python", new PythonParser()
    );
    
    public AnalysisResult analyze(Path path, String language) throws IOException {
        LanguageParser parser = parsers.get(language);
        if (parser == null) {
            throw new UnsupportedOperationException("Language not supported: " + language);
        }
        return parser.analyze(path);
    }
}
```

## Testing Strategy

### 1. Unit Testing
```java
@Test
void testNestedLoopDetection() {
    String code = """
        public class Test {
            public void method() {
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 10; j++) {
                        for (int k = 0; k < 10; k++) {
                            // Should be detected
                        }
                    }
                }
            }
        }
        """;
    
    AnalysisResult result = analyzer.analyze(createTempFile(code), "java");
    assertTrue(result.getIssues().stream()
        .anyMatch(issue -> issue.getDescription().contains("Deep nested loops")));
}
```

### 2. Integration Testing
```java
@Test
void testFullWorkflow() {
    // Test complete analyze -> suggest -> estimate workflow
    AnalysisResult analysis = analyzer.analyze(testFile, "java");
    SuggestionResult suggestions = suggester.suggest(testFile, "java");
    EstimateResult estimate = estimator.estimate(testFile, "java");
    
    // Verify consistency between results
    assertEquals(analysis.getIssues().size(), suggestions.getSuggestions().size());
    assertTrue(estimate.getPotentialSavings() > 0);
}
```

## Performance Considerations

### 1. Memory Usage
- **Stream Processing**: Use streams for large file sets
- **Lazy Loading**: Only parse files when needed
- **Object Pooling**: Reuse AST nodes where possible

### 2. CPU Optimization
- **Parallel Processing**: Use parallel streams for file analysis
- **Caching**: Cache parsed ASTs for repeated analysis
- **Early Termination**: Stop analysis when threshold reached

### 3. I/O Optimization
- **Batch Reading**: Read multiple files in batches
- **Buffered I/O**: Use buffered streams for file operations
- **Async Processing**: Process files asynchronously

---

This developer guide provides the foundation for understanding, extending, and maintaining the Forge system. The modular architecture and clear interfaces make it easy to add new features while maintaining code quality and performance.
