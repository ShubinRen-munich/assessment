 public class LimiterConfig {
    private String name;
    private ProductType productType;
    private PriceCalculation priceCalculation;
    private Scenario scenario;
    private double threshold;
    
    public LimiterConfig(String name, ProductType productType, PriceCalculation priceCalculation, Scenario scenario, double threshold) {
        this.name = name;
        this.productType = productType;
        this.priceCalculation = priceCalculation;
        this.scenario = scenario;
        this.threshold = threshold;
    }

    public String getName() {
        return name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public PriceCalculation getPriceCalculation() {
        return priceCalculation;
    }

    public Scenario getScenario() {
        return scenario;
    }   

    public double getThreshold() {
        return threshold;
    }
}