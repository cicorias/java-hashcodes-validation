import java.io.BufferedReader;
import java.io.FileReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.UUID;

public class App {

    public interface HashFunction {
        Integer call(String param, Integer buckets);
    }

    static HashFunction hash = (p1, p2) -> { return Math.abs(p1.hashCode()) % p2; };

    public static void main(String[] args) throws Exception {
        int slots = 31;
        int fileColumn = 1;
        String fileName = "dev-short.csv";

        // var rawStrings = readCsv(fileName, fileColumn);
        var rawStrings = readCsv("prod-devices.txt", 0, 10);
        // var rawStrings = generateDummyIds(100000);
        // var rawStrings = generateDummyIdsWithPrefix("spc", 100000);
        // var rawStrings = generateDummyIdsWithSuffix("spc", 100000);

        var foo = countOccuranceIntegers(rawStrings, slots);

        for (int i = 0; i < foo.length; i++) {
            System.out.println(MessageFormat.format("{0},{1,number,#}", i, foo[i]));
        }
    }

    static int[] countOccuranceIntegers(String[] results, int buckets) {
        int[] rv = new int[buckets];

        for (String s : results) {
            int r = hash.call(s, buckets);
            
            if (rv[r] == 0) {
                rv[r] = 1;
            }
            else {
                rv[r] += 1;
            }
        }

        return rv;
    }

    static String[] generateDummyIds(int howMany) {
        String[] rv = new String[howMany];

        for(int i = 0; i < rv.length; i++){
            rv[i] = UUID.randomUUID().toString();
        }
        return rv;
    }

    static String[] generateDummyIdsWithPrefix(String prefix, int howMany) {
        String[] rv = new String[howMany];

        for(int i = 0; i < rv.length; i++){
            rv[i] = MessageFormat.format("{0}-{1,number,#}", prefix, i);
        }
        return rv;
    }

    static String[] generateDummyIdsWithSuffix(String suffix, int howMany) {
        String[] rv = new String[howMany];

        for(int i = 0; i < rv.length; i++){
            rv[i] = MessageFormat.format("{1,number,#}-{0}", suffix, i);
        }
        return rv;
    }

    /** Read csv file into array */
    public static String[] readCsv(String fileName, int column, int trimStart) throws Exception {
        ArrayList<String> data = new ArrayList<String>();
        try {
            String line = "";
            String cvsSplitBy = ",";
            int row = 0;
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            while ((line = br.readLine()) != null) {
                if (row++ == 0) { // NOTE: skip header
                    continue;
                }
                String[] rowData = line.split(cvsSplitBy);
                data.add(rowData[column].substring(trimStart));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data.toArray(new String[data.size()]);
    }
}
