package assignment_java_core.model;

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
