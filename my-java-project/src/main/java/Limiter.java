public abstract class Limiter {
    private static final String buyHigherPassDescription = "buy higher, pass";
    private static final String buyLowerPassDescription = "buy lower, pass";
    private static final String sellHigherPassDescription = "sell higher, pass";
    private static final String sellLowerPassDescription = "sell lower, pass";
    public abstract EvaluationResult evaluate(Transaction transaction);
    public EvaluationResult evaluateByScenario(Transaction transaction, Scenario scenario, double diff, double threshold) {
        EvaluationResult result = new EvaluationResult(transaction);;
        if (scenario == Scenario.AtAdvantage) {
            if (transaction.getSide() == Side.Buy && diff > threshold) {
                result.setAlert(Alert.No);
                result.setDescription(buyHigherPassDescription);
            }
            if (transaction.getSide() == Side.Sell && diff < -threshold) {
                result.setAlert(Alert.No);
                result.setDescription(sellLowerPassDescription);
            }
        }
        if (scenario == Scenario.AtDisadvantage) {
            if (transaction.getSide() == Side.Buy && diff < -threshold) {
                result.setAlert(Alert.No);
                result.setDescription(buyLowerPassDescription);
            }
            if (transaction.getSide() == Side.Sell && diff > threshold) {
                result.setAlert(Alert.No);
                result.setDescription(sellHigherPassDescription);
            }
        }
        return result;
    }
}