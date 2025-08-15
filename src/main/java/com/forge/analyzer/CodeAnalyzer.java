package com.forge.analyzer;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CodeAnalyzer {
    
    public AnalysisResult analyze(Path path, String language) throws IOException {
        List<Issue> issues = new ArrayList<>();
        final int[] filesAnalyzed = {0};
        
        if (Files.isDirectory(path)) {
            // Analyze all Java files in directory
            Files.walk(path)
                .filter(p -> p.toString().endsWith(".java"))
                .forEach(file -> {
                    try {
                        issues.addAll(analyzeJavaFile(file));
                        filesAnalyzed[0]++;
                    } catch (IOException e) {
                        System.err.println("Warning: Could not analyze " + file + ": " + e.getMessage());
                    }
                });
        } else if (path.toString().endsWith(".java")) {
            issues.addAll(analyzeJavaFile(path));
            filesAnalyzed[0] = 1;
        }
        
        return new AnalysisResult(issues, filesAnalyzed[0]);
    }
    
    private List<Issue> analyzeJavaFile(Path file) throws IOException {
        List<Issue> issues = new ArrayList<>();
        String content = Files.readString(file);
        
        try {
            CompilationUnit cu = StaticJavaParser.parse(content);
            InefficiencyVisitor visitor = new InefficiencyVisitor(file.toString());
            cu.accept(visitor, issues);
        } catch (Exception e) {
            System.err.println("Warning: Could not parse " + file + ": " + e.getMessage());
        }
        
        return issues;
    }
    
    private static class InefficiencyVisitor extends VoidVisitorAdapter<List<Issue>> {
        private final String fileName;
        
        public InefficiencyVisitor(String fileName) {
            this.fileName = fileName;
        }
        
        @Override
        public void visit(MethodDeclaration md, List<Issue> issues) {
            super.visit(md, issues);
            
            // Check for deep nested loops
            checkNestedLoops(md, issues);
            
            // Check for string concatenation in loops
            checkStringConcatenationInLoops(md, issues);
            
            // Check for repeated method calls
            checkRepeatedMethodCalls(md, issues);
        }
        
        private void checkNestedLoops(MethodDeclaration md, List<Issue> issues) {
            md.findAll(ForStmt.class).forEach(forStmt -> {
                int nestingLevel = getNestingLevel(forStmt);
                if (nestingLevel > 2) {
                    issues.add(new Issue(
                        "Deep nested loops detected",
                        fileName + ":" + forStmt.getBegin().get().line,
                        "HIGH",
                        "Nested loops with depth " + nestingLevel + " can cause exponential time complexity"
                    ));
                }
            });
        }
        
        private void checkStringConcatenationInLoops(MethodDeclaration md, List<Issue> issues) {
            md.findAll(ForStmt.class).forEach(forStmt -> {
                forStmt.findAll(StringLiteralExpr.class).forEach(str -> {
                    if (str.getParentNode().isPresent() && 
                        str.getParentNode().get().toString().contains("+")) {
                        issues.add(new Issue(
                            "String concatenation in loop",
                            fileName + ":" + str.getBegin().get().line,
                            "MEDIUM",
                            "Consider using StringBuilder for string concatenation in loops"
                        ));
                    }
                });
            });
        }
        
        private void checkRepeatedMethodCalls(MethodDeclaration md, List<Issue> issues) {
            List<MethodCallExpr> methodCalls = md.findAll(MethodCallExpr.class);
            for (int i = 0; i < methodCalls.size(); i++) {
                for (int j = i + 1; j < methodCalls.size(); j++) {
                    MethodCallExpr call1 = methodCalls.get(i);
                    MethodCallExpr call2 = methodCalls.get(j);
                    
                    if (call1.getNameAsString().equals(call2.getNameAsString()) &&
                        call1.getArguments().size() == call2.getArguments().size()) {
                        issues.add(new Issue(
                            "Repeated method call detected",
                            fileName + ":" + call2.getBegin().get().line,
                            "MEDIUM",
                            "Consider caching result of " + call1.getNameAsString() + "() to avoid repeated computation"
                        ));
                    }
                }
            }
        }
        
        private int getNestingLevel(Statement stmt) {
            int level = 0;
            Statement current = stmt;
            while (current.getParentNode().isPresent()) {
                Node parentNode = current.getParentNode().get();
                if (parentNode instanceof ForStmt || parentNode instanceof WhileStmt || parentNode instanceof DoStmt) {
                    level++;
                    current = (Statement) parentNode;
                } else {
                    break;
                }
            }
            return level;
        }
    }
}
