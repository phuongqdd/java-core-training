package assignment_java_core.dto.request;

import java.time.LocalDateTime;

public class LogRequest {
    private String levelFilter;
    private String keywordFilter;
    private LocalDateTime fromTime;
    private LocalDateTime toTime;

    public LogRequest() {
    }

    public LogRequest(String levelFilter, String keywordFilter, LocalDateTime fromTime, LocalDateTime toTime) {
        this.levelFilter = levelFilter;
        this.keywordFilter = keywordFilter;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    public String getLevelFilter() {
        return levelFilter;
    }

    public void setLevelFilter(String levelFilter) {
        this.levelFilter = levelFilter;
    }

    public String getKeywordFilter() {
        return keywordFilter;
    }

    public void setKeywordFilter(String keywordFilter) {
        this.keywordFilter = keywordFilter;
    }

    public LocalDateTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalDateTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalDateTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalDateTime toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "LogRequest{" +
                "levelFilter='" + levelFilter + '\'' +
                ", keywordFilter='" + keywordFilter + '\'' +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                '}';
    }
}
