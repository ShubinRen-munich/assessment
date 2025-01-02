import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        try {
            FileReader fileReader = new FileReader(new File("C:\\Users\\sunki\\Documents\\GitHub\\assessment\\my-java-project\\config\\application.yaml"));
            Map<String, Object> configsMap = yaml.loadAs(fileReader, Map.class);
            Config config = new Config(configsMap);
            ReferenceManagement referenceManagement = new ReferenceManagement(config.getReferenceTable());
            TickManagement tickManagement = new TickManagement(config.getTickTable());
            LimiterManagement limiterManagement = new LimiterManagement(config.getLimiters(), referenceManagement, tickManagement);
            // Read file and Evaluate transactions
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Hello, World!");
    }
}