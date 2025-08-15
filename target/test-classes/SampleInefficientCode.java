public class SampleInefficientCode {
    
    public void inefficientMethod() {
        // Deep nested loops - HIGH severity issue
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                for (int k = 0; k < 100; k++) {
                    System.out.println("Processing: " + i + "," + j + "," + k);
                }
            }
        }
        
        // String concatenation in loop - MEDIUM severity issue
        String result = "";
        for (int i = 0; i < 1000; i++) {
            result += "item" + i + " ";
        }
        
        // Repeated method calls - MEDIUM severity issue
        for (int i = 0; i < 100; i++) {
            process(expensiveCalculation());
            validate(expensiveCalculation());
            log(expensiveCalculation());
        }
    }
    
    private String expensiveCalculation() {
        // Simulate expensive computation
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "expensive result";
    }
    
    private void process(String input) {
        // Process the input
    }
    
    private void validate(String input) {
        // Validate the input
    }
    
    private void log(String input) {
        // Log the input
    }
    
    public void efficientMethod() {
        // This method is efficient and should not trigger any issues
        for (int i = 0; i < 10; i++) {
            System.out.println("Efficient processing: " + i);
        }
    }
}
