public class Transaction {
    private int index;
    private String instrument;
    private Side side;
    private double price;

    public Transaction(int index, String instrument, Side side, double price) {
        this.index = index;
        this.instrument = instrument;
        this.side = side;
        this.price = price;
    }

    public int getIndex() {
        return index;
    }

    public String getInstrument() {
        return instrument;
    }

    public Side getSide() {
        return side;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.valueOf(index) + "," + instrument + "," + String.valueOf(side) + "," + String.valueOf(price);
    }
}
