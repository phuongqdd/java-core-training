package assignment_java_core.util;

import assignment_java_core.model.LogEntry;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {
    public static final DateTimeFormatter LOG_TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Chuyển String timestamp sang LocalDateTime
     */

    public static LocalDateTime parseTimestamp(String ts){
        return LocalDateTime.parse(ts, LOG_TIMESTAMP_FORMATTER);
    }

    /**
     * Định dạng LocalDateTime thành String log
     */
    public static String formatTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) return "";
        return timestamp.format(LOG_TIMESTAMP_FORMATTER);
    }

    /**
     * Tạo tên file kết quả xuất log có timestamp
     */
    public static String generateOutputFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "search_result_" + timestamp + ".txt";
    }

//    public static LogEntry parseLogLine(String line){
//        int firstBracket = line.indexOf("]");
//        String ts = line.substring(1, firstBracket);
//
//        int secondOpen = line.indexOf("[", firstBracket + 1);
//        int secondClose = line.indexOf("]", secondOpen);
//        String level = line.substring(secondOpen + 1, secondClose);
//
//        int thirdOpen = line.indexOf("[", secondClose + 1);
//        int thirdClose = line.indexOf("]", thirdOpen);
//        String service = line.substring(thirdOpen + 1, thirdClose);
//
//        String message = line.substring(line.indexOf("-", thirdClose) + 1).trim();
//
//        LocalDateTime timestamp = parseTimestamp(ts);
//        return new LogEntry(timestamp, level, service, message);
//    }

    public static LogEntry parseLogLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        line = line.trim();
        try {
            int tsStart = line.indexOf("[") + 1;
            int tsEnd = line.indexOf("]", tsStart);
            String timestamp = line.substring(tsStart, tsEnd);

            int levelStart = line.indexOf("[", tsEnd + 1) + 1;
            int levelEnd = line.indexOf("]", levelStart);
            String level = line.substring(levelStart, levelEnd);

            int svcStart = line.indexOf("[", levelEnd + 1) + 1;
            int svcEnd = line.indexOf("]", svcStart);
            String service = line.substring(svcStart, svcEnd);

            String message = line.substring(line.indexOf("-", svcEnd) + 2);

            return new LogEntry(parseTimestamp(timestamp), level, service, message);
        } catch (Exception e) {
            System.err.println("Không parse được dòng: " + line);
            return null;
        }
    }
}
