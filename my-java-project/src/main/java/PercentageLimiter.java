import java.text.NumberFormat;

public class PercentageLimiter extends Limiter {
    private Scenario scenario;
    private double threshold;
    private ReferenceManagement referenceManagement;
    private NumberFormat numberformat;

    // variation and description templates
    private static final String invalidReferenceVariation = "N/A";
    private static final String invalidReferenceDescription = "No reference price available";
    private static final String zeroReferenceDescription = "Reference price is zero";
    private static final String variationFormatter = "(%f - %f)/%f = %s";
    private static final String posPassDescription = "%s< %s, pass";
    private static final String negPassDescription = "abs(%s) < %s, pass";
    private static final String posBlockDescription = "%s >= %s, block";
    private static final String negBlockDescription = "abs(%s) >= %s, block";

    public PercentageLimiter(LimiterConfig config, ReferenceManagement referenceManagement) {
        this.referenceManagement = referenceManagement;

        this.scenario = config.getScenario();
        this.threshold = config.getThreshold() / 100.0f;
        this.numberformat = NumberFormat.getPercentInstance();
        this.numberformat.setMinimumFractionDigits(2);
    }

    @Override
    public EvaluationResult evaluate(Transaction transaction) {
        EvaluationResult result = new EvaluationResult(transaction);
        double price = transaction.getPrice();
        double referencePrice = referenceManagement.getReferencePrice(transaction.getInstrument());
        if (Double.isNaN(referencePrice)) {
            result.setAlert(Alert.Yes);
            result.setVariation(invalidReferenceVariation);
            result.setDescription(invalidReferenceDescription);
            return result;
        } 
        if (Math.abs(referencePrice) == 0.0001) {
            result.setAlert(Alert.Yes);
            result.setVariation(invalidReferenceVariation);
            result.setDescription(zeroReferenceDescription);
            return result;
        }
        double ratio = (price - referencePrice) / referencePrice;
        result.setVariation(String.format(variationFormatter, price, referencePrice, referencePrice, numberformat.format(ratio)));
        
        if (Math.abs(ratio) < threshold) {
            result.setAlert(Alert.No);
            referenceManagement.updateReferencePrice(transaction.getInstrument(), price);
            if (ratio > 0) {
                result.setDescription(String.format(posPassDescription, numberformat.format(ratio), numberformat.format(threshold)));
            }
            else {
                result.setDescription(String.format(negPassDescription, numberformat.format(ratio), numberformat.format(threshold)));
            } 
            return result;
        }

        EvaluationResult res = super.evaluateByScenario(transaction, scenario, ratio, threshold);
        if (res.getAlert() == Alert.No) {
            referenceManagement.updateReferencePrice(transaction.getInstrument(), price);
            res.setVariation(String.format(variationFormatter, price, referencePrice, referencePrice, numberformat.format(ratio)));
            return res;
        }
        result.setAlert(Alert.Yes);
        if (ratio > 0) {
            result.setDescription(String.format(posBlockDescription, numberformat.format(ratio), numberformat.format(threshold)));
        }
        else {
            result.setDescription(String.format(negBlockDescription, numberformat.format(ratio), numberformat.format(threshold)));
        }     
        return result;
    }

}
