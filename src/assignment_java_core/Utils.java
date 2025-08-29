package assignment_java_core;

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
     * Kiểm tra xem timestamp có nằm trong khoảng from-to không
     * Nếu from/to null thì coi như không giới hạn
     */
    public static boolean isInRange(LocalDateTime timestamp, LocalDateTime from, LocalDateTime to) {
        if (timestamp == null) return false;
        if (from != null && timestamp.isBefore(from)) return false;
        if (to != null && timestamp.isAfter(to)) return false;
        return true;
    }

    /**
     * Định dạng LocalDateTime thành String log
     */
    public static String formatTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) return "";
        return timestamp.format(LOG_TIMESTAMP_FORMATTER);
    }

    /**
     * Tạo tên file kết quả xuất log có timestamp, ví dụ: search_result_20250828_235012.txt
     */
    public static String generateOutputFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return "search_result_" + timestamp + ".txt";
    }

    public static LogEntry parseLogLine(String line){
        int firstBracket = line.indexOf("]");
        String ts = line.substring(1, firstBracket);

        int secondOpen = line.indexOf("[", firstBracket + 1);
        int secondClose = line.indexOf("]", secondOpen);
        String level = line.substring(secondOpen + 1, secondClose);

        int thirdOpen = line.indexOf("[", secondClose + 1);
        int thirdClose = line.indexOf("]", thirdOpen);
        String service = line.substring(thirdOpen + 1, thirdClose);

        String message = line.substring(line.indexOf("-", thirdClose) + 1).trim();

        LocalDateTime timestamp = parseTimestamp(ts);
        return new LogEntry(timestamp, level, service, message);
    }

}
