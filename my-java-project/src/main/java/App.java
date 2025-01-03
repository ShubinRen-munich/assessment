import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Yaml yaml = new Yaml();
        try {
            //TODO: change config C:\\Users\\sunki\\Documents\\GitHub\\assessment\\my-java-project\\
            FileReader fileReader = new FileReader(new File("config\\application.yaml"));
            Map<String, Object> configsMap = yaml.loadAs(fileReader, Map.class);
            Config config = new Config(configsMap);
            ReferenceManagement referenceManagement = new ReferenceManagement(config.getReferenceTable());
            TickManagement tickManagement = new TickManagement(config.getTickTable());
            LimiterManagement limiterManagement = new LimiterManagement(config.getLimiters(), referenceManagement, tickManagement);
            // Read file and Evaluate transactions
            String inputFilePath1 = "data\\input\\option.csv";
            String outputFilePath1 = "data\\output\\option.csv";
            evaluateOneFile(inputFilePath1, outputFilePath1, limiterManagement);
            String inputFilePath2 = "data\\input\\stock.csv";
            String outputFilePath2 = "data\\output\\stock.csv";
            evaluateOneFile(inputFilePath2, outputFilePath2, limiterManagement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void evaluateOneFile(String inputFilePath, String outputFilePath, LimiterManagement limiterManagement) {
        // Read file and Evaluate transactions
        List<Transaction> transactions = readTransactions(inputFilePath);
        List<EvaluationResult> results = new ArrayList<>();
        for (Transaction transaction : transactions) {
            EvaluationResult result = limiterManagement.evaluate(transaction);
            results.add(result);
        }
        writeResults(outputFilePath, results);
    }

    public static List<Transaction> readTransactions(String inputFilePath) {
        // Read file and return transactions
        List<Transaction> transactions = new ArrayList<>();
        File file = new File(inputFilePath);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) {
                    continue;
                }
                if (parts[1].equals("Instrument")) { //headline
                    continue;
                }
                Transaction transaction = new Transaction(Integer.parseInt(parts[0]), parts[1], Side.valueOf(parts[2]),Double.parseDouble(parts[3]));
                transactions.add(transaction);
            }
            br.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static void writeResults(String outputFilePath, List<EvaluationResult> results) {
        // Write results to file
        File file = new File(outputFilePath);
        try {
            java.io.FileWriter fw = new java.io.FileWriter(file);
            fw.write("TransactionID,Instrument,Side,Price,Alert,Variation,Description\n");
            for (EvaluationResult result : results) {
                fw.write(result.toString() + "\n");
            }
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}