public class EvaluationResult {
    private Transaction transaction;
    private Alert alert;
    private String variation;
    private String Description;

    public EvaluationResult(Transaction transaction) {
        this.transaction = transaction;
        this.alert = Alert.Invalid;
        this.variation = "";
        this.Description = "";
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Alert getAlert() {
        return alert;
    }

    public String getVariation() {
        return variation;
    }

    public String getDescription() {
        return Description;
    }

    public void setAlert(Alert alert) {
        this.alert = alert;
    }
    
    public void setVariation(String variation) {
        this.variation = variation;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }
}

