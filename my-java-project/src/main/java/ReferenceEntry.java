public class ReferenceEntry {
    private String instrument;
    private ProductType productType;
    private double theoPrice;
    private double lastTradePrice;
    private double closePrice;

    public ReferenceEntry(String instrument, ProductType productType, double theoPrice, double lastTradePrice, double closePrice) {
        this.instrument = instrument;
        this.productType = productType;
        this.theoPrice = theoPrice;
        this.lastTradePrice = lastTradePrice;
        this.closePrice = closePrice;
    }

    public String getInstrument() {
        return instrument;
    }

    public ProductType getProductType() {
        return productType;
    }

    public double getTheoPrice() {
        return theoPrice;
    }

    public double getLastTradePrice() {
        return lastTradePrice;
    }

    public double getClosePrice() {
        return closePrice;
    }
}