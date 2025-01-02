public class LimiterFactory {
    public static Limiter createLimiter(LimiterConfig config, ReferenceManagement referenceManagement, TickManagement tickManagement) {
        switch (config.getPriceCalculation()) {
            case ByValue:
                return new ValueLimiter(config, referenceManagement);
            case ByTick:
                return new TickLimiter(config, referenceManagement, tickManagement);
            case ByPercentage:
                return new PercentageLimiter(config, referenceManagement);
            default:
                return null;
        }
    }
}
