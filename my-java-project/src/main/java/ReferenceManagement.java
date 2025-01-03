import java.util.HashMap;
import java.util.List;

public class ReferenceManagement {
    private HashMap<String, HashMap<String, Double>> referenceMap;
    private HashMap<String, ProductType> productTypeMap;
    
    public ReferenceManagement(List<ReferenceEntry> referenceEntries) {
        this.referenceMap = new HashMap<>();
        this.productTypeMap = new HashMap<>();
        for (ReferenceEntry referenceEntry : referenceEntries) {
            String instrument = referenceEntry.getInstrument();
            double theoPrice = referenceEntry.getTheoPrice();
            double lastTradePrice = referenceEntry.getLastTradePrice();
            double closePrice = referenceEntry.getClosePrice();
            if (!referenceMap.containsKey(instrument)) {
                referenceMap.put(instrument, new HashMap<>());
            }
            if (!Double.isNaN(theoPrice)) {
                referenceMap.get(instrument).put("theoPrice", theoPrice);
            }
            if (!Double.isNaN(lastTradePrice)) {
                referenceMap.get(instrument).put("lastTradePrice", lastTradePrice);
            }
            if (!Double.isNaN(closePrice)) {
                referenceMap.get(instrument).put("closePrice", closePrice);
            }
            productTypeMap.put(instrument, referenceEntry.getProductType());
        }
    }

    public double getReferencePrice(String instrument) {
        if (!referenceMap.containsKey(instrument)) {
            return Double.NaN;
        }
        if (referenceMap.get(instrument).containsKey("lastTradePrice")) {
            return referenceMap.get(instrument).get("lastTradePrice");
        }
        if (referenceMap.get(instrument).containsKey("closePrice")) {
            return referenceMap.get(instrument).get("closePrice");
        }
        if (referenceMap.get(instrument).containsKey("theoPrice")) {
            return referenceMap.get(instrument).get("theoPrice");
        }    
        return Double.NaN;
    }

    public void updateReferencePrice(String instrument, double price) {
        if (!referenceMap.containsKey(instrument)) {
            referenceMap.put(instrument, new HashMap<>());
        }
        referenceMap.get(instrument).put("lastTradePrice", price);
    }

    public ProductType getProductType(String instrument) {
        return productTypeMap.get(instrument);
    }
}
