package com.forge.analyzer;

import java.util.List;

public class AnalysisResult {
    private final List<Issue> issues;
    private final int filesAnalyzed;
    
    public AnalysisResult(List<Issue> issues, int filesAnalyzed) {
        this.issues = issues;
        this.filesAnalyzed = filesAnalyzed;
    }
    
    public List<Issue> getIssues() {
        return issues;
    }
    
    public int getFilesAnalyzed() {
        return filesAnalyzed;
    }
}
