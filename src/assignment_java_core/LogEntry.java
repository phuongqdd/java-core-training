package assignment_java_core;

import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private String level;
    private String service;
    private String message;

    public LogEntry() {
    }

    public LogEntry(LocalDateTime timestamp, String level, String service, String message) {
        this.timestamp = timestamp;
        this.level = level;
        this.service = service;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean matches(String level,
                    LocalDateTime from, LocalDateTime to,
                    String keyword) {
        // Lọc level
        if (level != null && !this.getLevel().equalsIgnoreCase(level)) return false;

        // Lọc khoảng thời gian
        if (!Utils.isInRange(this.getTimestamp(), from, to)) return false;

        // Lọc keyword trong message
        if (keyword != null && !this.getMessage().toLowerCase().contains(keyword.toLowerCase())) return false;

        return true;
    }
    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp=" + timestamp +
                ", level='" + level + '\'' +
                ", service='" + service + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
