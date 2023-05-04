package Resources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateCSV {
    private static final int SIZE = 100;
    private static final String PATH = "test.csv";
    public static void main(String[] args) throws IOException {
        File f = new File(PATH);
        FileWriter fw = new FileWriter(f, false);
        for(int i = 0; i < SIZE; i++) {
            fw.write((Math.random() < 0.5 ? "0" : "1") + ",");
        }
        fw.close();
    }
}
