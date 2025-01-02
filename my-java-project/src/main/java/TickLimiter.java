import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TickLimiter extends Limiter {

    private class TickResult {
        public int tick;
        public double start;
        public double end;
    }

    private Scenario scenario;
    private double threshold;
    private ReferenceManagement referenceManagement;
    private TickManagement tickManagement;


     // variation and description templates
     private static final String invalidReferenceVariation = "N/A";
     private static final String invalidReferenceDescription = "No reference price available";
     private static final String noTickConfigFoundDescription = "tick config not found";
     private static final String tickVariationFormatter = "(%f-%f)/%f";
     private static final String posPassDescription = "%d< %s, pass";
     private static final String negPassDescription = "abs(%d) < %s, pass";
     private static final String posBlockDescription = "%d >= %s, block";
     private static final String negBlockDescription = "abs(%d) >= %s, block";

    public TickLimiter(LimiterConfig config, ReferenceManagement referenceManagement, TickManagement tickManagement) {
        this.referenceManagement = referenceManagement;
        this.tickManagement = tickManagement;
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

        List<TickConfig> tickConfigs = tickManagement.getTickConfigs(transaction.getInstrument());
        if (tickConfigs == null) {
            result.setAlert(Alert.Yes);
            result.setVariation(invalidReferenceVariation);
            result.setDescription(noTickConfigFoundDescription);
            return result;
        }

        List<TickResult> tickResults;
        if (price > referencePrice) {
            tickResults = getTickResults(referencePrice, price, tickConfigs);
        } else {
            tickResults = getTickResults(price, referencePrice, tickConfigs);
            Collections.reverse(tickResults);
        }

        int sum = 0;
        for (TickResult tickResult : tickResults) {
            if (referencePrice > price) {
                sum += tickResult.tick;
            } else {
                sum -= tickResult.tick;
            }
        }
        
        result.setVariation(buildVariation(tickResults, sum));
        if (Math.abs(sum) < threshold) {
            result.setAlert(Alert.No);
            referenceManagement.updateReferencePrice(transaction.getInstrument(), price);
            if (sum > 0) {
                result.setDescription(String.format(posPassDescription, sum, threshold));
            } else {
                result.setDescription(String.format(negPassDescription, sum, threshold));
            }
            return result;
        }

        EvaluationResult res = super.evaluateByScenario(transaction, scenario, sum, threshold);
        if (res.getAlert() == Alert.No) {
            referenceManagement.updateReferencePrice(transaction.getInstrument(), price);
            res.setVariation(result.getVariation());
            return res;
        }

        result.setAlert(Alert.Yes);
        if (sum > 0) {
            result.setDescription(String.format(posBlockDescription, sum, threshold));
        } else {
            result.setDescription(String.format(negBlockDescription, sum, threshold));
        }
        return result;
    }

    private List<TickResult> getTickResults(double start, double end, List<TickConfig> tickConfigs) {
        List<TickResult> tickResults = new ArrayList<>();
        for (TickConfig tickConfig : tickConfigs) {
            TickResult tickResult = new TickResult();
            if(tickConfig.getMax() <= start) {
                continue;
            }
            if(tickConfig.getMax() >= end) {
                tickResult.start = start;
                tickResult.end = end;
                tickResult.tick = (int)Math.ceil((end - start) / tickConfig.getTick());
                tickResults.add(tickResult);
                break;
            }
            tickResult.start = start;
            tickResult.end = tickConfig.getMax();
            tickResult.tick = (int)Math.ceil((tickConfig.getMax() - start) / tickConfig.getTick());
            tickResults.add(tickResult);
            start = tickConfig.getMax();
        }
        return tickResults;
    }

    private String buildVariation(List<TickResult> tickResults, int sum) {
        StringBuilder variation = new StringBuilder();
        for( int i = 0; i < tickResults.size(); i++) {
            TickResult tickResult = tickResults.get(i);
            if (i == 0) {
                variation.append(String.format(tickVariationFormatter, tickResult.start, tickResult.end, tickResult.tick));
            } else {
                variation.append(" + ");
                variation.append(String.format(tickVariationFormatter, tickResult.start, tickResult.end, tickResult.tick));
            }
        }
        
        variation.append(" = ");
        variation.append(String.valueOf(sum));
        return variation.toString();
    }
}
