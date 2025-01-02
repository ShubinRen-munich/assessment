import java.util.Map;
import java.util.List;
public class TickManagement {
    private Map<String, List<TickConfig>> tickTable;
    public TickManagement(List<TickTableEntry> tickTableEntries) {
        for (TickTableEntry tickTableEntry : tickTableEntries) {
            tickTable.put(tickTableEntry.getInstrument(), tickTableEntry.getTickConfigs());
        }
    }
    public List<TickConfig> getTickConfigs(String instrument) {
        return tickTable.get(instrument);
    }
}
