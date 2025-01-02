public class ValueLimiter extends Limiter {
    private Scenario scenario;
    private double threshold;
    private ReferenceManagement referenceManagement;

    // variation and description templates
    private static final String invalidReferenceVariation = "N/A";
    private static final String invalidReferenceDescription = "No reference price available";
    private static final String variationFormatter = "%f - %f = %f";
    private static final String posPassDescription = "%f < %f, pass";
    private static final String negPassDescription = "abs(%f) < %f, pass";
    private static final String posBlockDescription = "%f >= %f, block";
    private static final String negBlockDescription = "abs(%f) >= %f, block";

    public ValueLimiter(LimiterConfig config, ReferenceManagement referenceManagement) {
        this.referenceManagement = referenceManagement;
        this.scenario = config.getScenario();
        this.threshold = config.getThreshold();
    }

    @Override
    public EvaluationResult evaluate(Transaction transaction) {
        double price = transaction.getPrice();
        double referencePrice = referenceManagement.getReferencePrice(transaction.getInstrument());
        EvaluationResult result = new EvaluationResult(transaction);
        if (Double.isNaN(referencePrice)) {
            result.setAlert(Alert.Yes);
            result.setVariation(invalidReferenceVariation);
            result.setDescription(invalidReferenceDescription);
            return result;
        } 

        double difference = price - referencePrice;
        result.setVariation(String.format(variationFormatter, referencePrice, price, difference));
        if (Math.abs(difference) < threshold) {
            result.setAlert(Alert.No);
            referenceManagement.updateReferencePrice(transaction.getInstrument(), price);
            if (difference > 0) {
                result.setDescription(String.format(posPassDescription, difference, threshold));
            }
            else {
                result.setDescription(String.format(negPassDescription, difference, threshold));
            } 
            return result;
        } 
        
        EvaluationResult res = super.evaluateByScenario(transaction, scenario, difference, threshold);
        if (res.getAlert() == Alert.No) {
            referenceManagement.updateReferencePrice(transaction.getInstrument(), price);
            res.setVariation(String.format(variationFormatter, referencePrice, price, difference));
            return res;
        }
    
        result.setAlert(Alert.Yes);
        if (difference > 0) {
            result.setDescription(String.format(posBlockDescription, difference, threshold));
        }
        else {
            result.setDescription(String.format(negBlockDescription, difference, threshold));
        }        

        return result;
    }
}
