package assignment_java_core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ThuNghiem {
    public static void main(String[] args) {
        try (BufferedReader file = Files.newBufferedReader(
                Paths.get("src", "assignment_java_core", "system_logs.log"))) {

            long start = System.currentTimeMillis();
            String line;
            while ((line = file.readLine()) != null) {
                int i = 0; // giả lập xử lý
            }
            long end = System.currentTimeMillis();
            System.out.println("ArrayList add time: " + (end - start) + " ms");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
