import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class LimiterManagement {
    private ReferenceManagement referenceManagement;
    @SuppressWarnings("unused")
    private TickManagement tickManagement;
    private Map<ProductType, Limiter> limiters;

    public LimiterManagement(List<LimiterConfig> configs, ReferenceManagement referenceManagement, TickManagement tickManagement) {
        this.referenceManagement = referenceManagement;
        this.tickManagement = tickManagement;
        this.limiters = new HashMap<>();
        for (LimiterConfig config : configs) {
            ProductType productType = config.getProductType();
            Limiter limiter = LimiterFactory.createLimiter(config, referenceManagement, tickManagement);
            if (limiter == null) {
                throw new IllegalArgumentException("Invalid price calculation type");
            }
            limiters.put(productType, limiter);
        }
    }

    public EvaluationResult evaluate(Transaction transaction) {
        ProductType productType = this.referenceManagement.getProductType(transaction.getInstrument());
        Limiter limiter = limiters.get(productType);
        if (limiter == null) {
            throw new IllegalArgumentException("No limiter found for product type");
        }
        return limiter.evaluate(transaction);
    }

}
