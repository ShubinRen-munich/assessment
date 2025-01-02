import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {
    private List<LimiterConfig> limiters;
    private List<TickTableEntry> tickTable;
    private List<ReferenceEntry> referenceTable;

    public Config(Map<String, Object> configsMap) {
        List<Map<String, Object>> limiters = castListOfMaps(configsMap.get("limiters"));
        List<Map<String, Object>> tickTable = castListOfMaps(configsMap.get("tickTable"));
        List<Map<String, Object>> referenceTable = castListOfMaps(configsMap.get("referenceTable"));

        this.limiters = limiters.stream().map(limiter -> new LimiterConfig(
                (String) limiter.get("name"),
                ProductType.valueOf((String) limiter.get("productType")),
                PriceCalculation.valueOf((String) limiter.get("priceCalculation")),
                Scenario.valueOf((String) limiter.get("scenario")),
                (double) limiter.get("threshold")
        )).collect(Collectors.toList());

        this.tickTable = tickTable.stream().map(tickTableEntry -> new TickTableEntry(
                (String) tickTableEntry.get("instrument"),
                castListOfMaps(tickTableEntry.get("tickConfigs")).stream().map(tickConfig -> new TickConfig(
                        (double) tickConfig.get("min"),
                        (double) tickConfig.get("tick"),
                        tickConfig.get("max") != null ? (double) tickConfig.get("max") : Double.MAX_VALUE
                )).collect(Collectors.toList())
        )).collect(Collectors.toList());

        this.referenceTable = referenceTable.stream().map(referenceEntry -> new ReferenceEntry(
                (String) referenceEntry.get("instrument"),
                ProductType.valueOf((String) referenceEntry.get("productType")),
                (double) referenceEntry.get("theoPrice"),
                (double) referenceEntry.get("lastTradePrice"),
                (double) referenceEntry.get("closePrice")
        )).collect(Collectors.toList());
    }

    public List<LimiterConfig> getLimiters() {
        return limiters;
    }

    public List<TickTableEntry> getTickTable() {
        return tickTable;
    }

    public List<ReferenceEntry> getReferenceTable() {
        return referenceTable;
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castListOfMaps(Object obj) {
        if (obj instanceof List<?>) {
            return ((List<?>) obj).stream()
                    .filter(item -> item instanceof Map)
                    .map(item -> (Map<String, Object>) item)
                    .collect(Collectors.toList());
        }
        throw new ClassCastException("Cannot cast to List<Map<String, Object>>");
    }
}
