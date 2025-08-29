package assignment_java_core;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Scanner;

public class LogSearchApp {
    public static void main(String[] args) {
        String inputFile = "src/assignment_java_core/system_logs.log";
        String outputFile = "";

        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Tìm kiếm log ===");

        String levelFilter = null;
        String keywordFilter = null;
        LocalDateTime fromTime = null;
        LocalDateTime toTime = null;

        while (true) {
            System.out.print("Nhập log level (INFO/WARN/ERROR) hoặc Enter bỏ qua: ");
            levelFilter = scanner.nextLine().trim();
            if (levelFilter.isEmpty()) levelFilter = null;

            System.out.print("Nhập từ khóa trong message hoặc Enter bỏ qua: ");
            keywordFilter = scanner.nextLine().trim();
            if (keywordFilter.isEmpty()) keywordFilter = null;

            System.out.print("Nhập thời gian từ (yyyy-MM-dd HH:mm:ss.SSS) hoặc Enter bỏ qua: ");
            String fromStr = scanner.nextLine().trim();
            if (!fromStr.isEmpty()) fromTime = Utils.parseTimestamp(fromStr);

            System.out.print("Nhập thời gian đến (yyyy-MM-dd HH:mm:ss.SSS) hoặc Enter bỏ qua: ");
            String toStr = scanner.nextLine().trim();
            if (!toStr.isEmpty()) toTime = Utils.parseTimestamp(toStr);

            // Kiểm tra nếu tất cả null thì yêu cầu nhập lại
            if (levelFilter != null || keywordFilter != null || fromTime != null || toTime != null) {
                break; // có ít nhất 1 trường → thoát vòng lặp
            } else {
                System.out.println("Bạn phải nhập ít nhất một tiêu chí tìm kiếm. Vui lòng thử lại.");
            }
        }

        int count = 0;
        StringBuilder resultBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(inputFile))){
            String line;
            while ((line = br.readLine()) != null){
                LogEntry log = Utils.parseLogLine(line);
                if(log.matches(levelFilter, fromTime, toTime, keywordFilter)){
                    resultBuilder.append(log.toString()).append(System.lineSeparator());
                    count++;
                }
            }
            System.out.println("Tìm thấy " + count + " lo thỏa điều kiện.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(count > 0){
            System.out.print("Bạn có muốn xuất kết quả ra file không? (Y/N): ");
            String choice = scanner.nextLine().trim();
            if (choice.equalsIgnoreCase("Y")) {
                outputFile = Utils.generateOutputFileName();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
                    bw.write(resultBuilder.toString());
                    System.out.println("Kết quả đã lưu vào file: " + outputFile);
                } catch (IOException e) {
                    System.err.println("Lỗi khi ghi file: " + e.getMessage());
                }
            } else {
                System.out.println("Kết quả tìm kiếm không được lưu file.");
            }
        } else {
            System.out.println("Không có log nào thỏa điều kiện. Không xuất file.");
        }

        scanner.close();
    }
}
