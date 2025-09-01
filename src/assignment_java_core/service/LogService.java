package assignment_java_core.service;

import assignment_java_core.model.LogEntry;

import java.time.LocalDateTime;
import java.util.List;

public interface LogService {
    boolean checkLog(LogEntry log, String level,
                       LocalDateTime from, LocalDateTime to,
                       String keyword);

    List<LogEntry> searchLogs(String level,
                              LocalDateTime from, LocalDateTime to,
                              String keyword);

    void exportLogs(List<LogEntry> logs);
}
