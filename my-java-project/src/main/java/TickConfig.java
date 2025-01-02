public class TickConfig {
    private double min;
    private double max;
    private double tick;

    public TickConfig(double min, double tick, double max) {
        this.min = min;
        this.max = max;
        this.tick = tick;
    }

    public TickConfig(double min, double tick) {
        this(min, Double.MAX_VALUE, tick);
    }

    public double getMin() {
        return min;
    }
    
    public double getMax() {
        return max;
    }

    public double getTick() {
        return tick;
    }
}
