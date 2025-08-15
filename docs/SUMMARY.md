# Forge - Quick Summary 🚀

## What is Forge?
Forge is a **Java CLI tool** that analyzes codebases for inefficient patterns and suggests greener, energy-efficient alternatives. It helps developers write more sustainable software by detecting performance issues and estimating their environmental impact.

## Key Features
- 🔍 **Code Analysis**: Detects inefficient patterns in Java code
- 💡 **Smart Suggestions**: Provides specific optimization recommendations
- 🌱 **Carbon Estimation**: Calculates energy usage and CO₂ emissions
- 📊 **Multiple Outputs**: Text and JSON formats
- ⚡ **Fast & Lightweight**: Built with Java 21

## How It Works
Forge uses **rule-based static analysis** (not AI) to detect specific inefficient patterns:

1. **Parses** Java code into Abstract Syntax Trees
2. **Applies** predefined rules to detect inefficiencies
3. **Generates** specific optimization suggestions
4. **Estimates** carbon footprint and potential savings

## Current Capabilities

### Detected Issues
- **Deep Nested Loops**: O(n³) complexity patterns
- **String Concatenation in Loops**: Inefficient string building
- **Repeated Method Calls**: Unnecessary computation

### Language Support
- ✅ **Java**: Full support (current)
- 🔄 **Python**: Planned (not implemented)
- 🔄 **JavaScript**: Planned (not implemented)

## Quick Start

### Build
```bash
cd forge
mvn clean package
```

### Use
```bash
# Analyze code
java -jar target/forge-1.0.0.jar analyze ./myproject --lang java

# Get suggestions
java -jar target/forge-1.0.0.jar suggest ./myproject --lang java

# Estimate carbon footprint
java -jar target/forge-1.0.0.jar estimate ./myproject --lang java
```

## Example Output

### Analysis
```
🔍 Analysis Results for ./TestInefficientCode.java
Language: java
Files analyzed: 1
Issues found: 15

⚠️  Inefficiencies found:
  • String concatenation in loop
    Location: ./TestInefficientCode.java:8
    Severity: MEDIUM
```

### Suggestions
```
💡 Optimization Suggestions for ./TestInefficientCode.java
🔧 Use StringBuilder for string concatenation
   Description: Replace string concatenation with StringBuilder
   Impact: MEDIUM
   Example:
     Before: String result = ""; for (int i = 0; i < n; i++) { result += i; }
     After:  StringBuilder result = new StringBuilder(); for (int i = 0; i < n; i++) { result.append(i); }
```

### Carbon Estimation
```
🌱 Carbon Footprint Estimate for ./TestInefficientCode.java
📊 Current Estimate:
  • CPU Time: 154.07 ms
  • Energy Usage: 36.0524 Wh
  • CO₂ Emissions: 18026.215242 g CO₂

💚 Potential Savings (with optimizations):
  • Energy Savings: 3.3000 Wh
  • CO₂ Reduction: 1650.000000 g CO₂
  • Percentage: 9.2%
```

## Architecture Overview

```
CLI Commands → Code Analyzer → Rule Engine → Suggestions → Carbon Estimator
     ↓              ↓              ↓            ↓              ↓
  User Input   AST Parsing   Pattern Match  Optimization  Energy Model
```

## Intelligence Level
- **NOT AI**: Uses rule-based pattern matching
- **Deterministic**: Same code always produces same results
- **Transparent**: Rules are explicit and understandable
- **Fast**: No model loading or inference time

## Carbon Estimation Model
```
Estimated CPU Time (ms) × CPU Power (65W) × CO₂/kWh (0.5 kg) = CO₂ Emissions
```

**Factors:**
- File count and complexity
- Detected inefficiencies (severity-based penalties)
- Global energy and carbon averages

## Documentation
- **[Usage Guide](./USAGE_GUIDE.md)**: How to use Forge
- **[Developer Guide](./DEVELOPER_GUIDE.md)**: How to extend Forge
- **[System Overview](./SYSTEM_OVERVIEW.md)**: How Forge works internally
- **[Project Plan](./project-plan.md)**: Original project specification

## Limitations
- Only Java is fully supported
- Estimates are approximations
- Limited to specific patterns
- No runtime analysis
- No database optimization

## Future Enhancements
- Python and JavaScript support
- Machine learning-based analysis
- IDE plugin integration
- Database query optimization
- Memory usage analysis
- GitHub Actions integration

## Why Forge?
- **Immediate Value**: Fast analysis with clear results
- **Environmental Impact**: Quantified energy and carbon savings
- **Developer Friendly**: Actionable suggestions with examples
- **Extensible**: Easy to add new rules and languages
- **Production Ready**: Reliable and deterministic

---

**Forge helps developers write greener code, one optimization at a time! 🌱**
