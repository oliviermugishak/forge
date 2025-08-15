# Forge 🔥

A Java CLI tool that analyzes codebases, detects inefficient patterns, and suggests greener, energy-efficient alternatives — helping developers write more sustainable software.

## Features

- **🔍 Code Analysis**: Detects inefficient patterns in Java code
- **💡 Optimization Suggestions**: Provides specific recommendations for improvements
- **🌱 Carbon Footprint Estimation**: Calculates energy usage and CO₂ emissions
- **📊 Multiple Output Formats**: Text and JSON output support
- **⚡ Fast & Lightweight**: Built with Java 21 and optimized for performance

## Installation

### Prerequisites

- Java 21 or higher
- Maven 3.6+

### Build from Source

```bash
git clone <repository-url>
cd forge
mvn clean package
```

The executable JAR will be created at `target/forge-1.0.0.jar`.

## Usage

### Basic Commands

```bash
# Analyze code for inefficiencies
java -jar forge.jar analyze <path> --lang java

# Get optimization suggestions
java -jar forge.jar suggest <path> --lang java

# Estimate carbon footprint
java -jar forge.jar estimate <path> --lang java
```

### Command Options

#### Analyze Command
```bash
forge analyze <path> [--lang java|python|javascript]
```

#### Suggest Command
```bash
forge suggest <path> [--lang java|python|javascript] [--output text|json]
```

#### Estimate Command
```bash
forge estimate <path> [--lang java|python|javascript] [--output text|json]
```

### Examples

#### Analyze a Java Project
```bash
java -jar forge.jar analyze ./myproject --lang java
```

Output:
```
🔍 Analysis Results for ./myproject
Language: java
Files analyzed: 5
Issues found: 3

⚠️  Inefficiencies found:
  • Deep nested loops detected
    Location: ./myproject/Processor.java:15
    Severity: HIGH

  • String concatenation in loop
    Location: ./myproject/Formatter.java:23
    Severity: MEDIUM

  • Repeated method call detected
    Location: ./myproject/Validator.java:8
    Severity: MEDIUM
```

#### Get Optimization Suggestions
```bash
java -jar forge.jar suggest ./myproject --lang java
```

Output:
```
💡 Optimization Suggestions for ./myproject
Language: java
Suggestions found: 3

🔧 Use divide-and-conquer algorithm
   Description: Replace nested loops with a more efficient algorithm like divide-and-conquer
   Location: ./myproject/Processor.java:15
   Impact: HIGH
   Example:
     Before: for (int i = 0; i < n; i++) {
       for (int j = 0; j < n; j++) {
         for (int k = 0; k < n; k++) {
           // O(n³) complexity
         }
       }
     }
     After:  // Use divide-and-conquer or dynamic programming
// Example: Merge sort, Quick sort, or matrix multiplication algorithms
```

#### Estimate Carbon Footprint
```bash
java -jar forge.jar estimate ./myproject --lang java
```

Output:
```
🌱 Carbon Footprint Estimate for ./myproject
Language: java

📊 Current Estimate:
  • CPU Time: 150.00 ms
  • Energy Usage: 0.0054 Wh
  • CO₂ Emissions: 0.0027 g CO₂

💚 Potential Savings (with optimizations):
  • Energy Savings: 0.0022 Wh
  • CO₂ Reduction: 0.0011 g CO₂
  • Percentage: 40.7%
```

#### JSON Output
```bash
java -jar forge.jar suggest ./myproject --lang java --output json
```

## Detected Issues

Forge currently detects the following inefficient patterns:

### High Severity
- **Deep Nested Loops**: Loops with nesting depth > 2 that can cause exponential time complexity

### Medium Severity
- **String Concatenation in Loops**: Using `+` operator for string concatenation in loops
- **Repeated Method Calls**: Calling the same expensive method multiple times with same parameters

## Carbon Estimation Model

Forge uses a simplified model to estimate carbon footprint:

```
Estimated CPU Time (ms) × Avg CPU Power (65W) × CO₂/kWh (0.5 kg)
```

The model considers:
- Average CPU power consumption: 65W
- Global average CO₂ emissions: 0.5 kg CO₂ per kWh
- Code complexity penalties based on detected issues

## Development

### Project Structure
```
forge/
├── src/main/java/com/forge/
│   ├── cli/               # CLI entry points
│   ├── analyzer/          # Code analysis engine
│   ├── suggestions/       # Optimization recommendations
│   ├── carbon/           # Carbon estimation logic
│   └── ForgeMain.java    # Main entry point
├── src/test/java/        # Unit tests
└── pom.xml              # Maven configuration
```

### Running Tests
```bash
mvn test
```

### Building
```bash
mvn clean package
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Run the test suite
6. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Roadmap

- [ ] Python and JavaScript support
- [ ] IDE plugin integration
- [ ] GitHub Action for CI/CD
- [ ] More sophisticated carbon estimation models
- [ ] Performance profiling integration
- [ ] Database query optimization detection
