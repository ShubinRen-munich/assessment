import java.util.List;

public class TickTableEntry {
    private String instrument;
    private List<TickConfig> tickConfigs;

    public TickTableEntry(String instrument, List<TickConfig> tickConfigs) {
        this.instrument = instrument;
        this.tickConfigs = tickConfigs;
    }

    public String getInstrument() {
        return instrument;
    }

    public List<TickConfig> getTickConfigs() {
        return tickConfigs;
    }
}