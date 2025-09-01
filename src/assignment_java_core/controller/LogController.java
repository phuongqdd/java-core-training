package assignment_java_core.controller;

import assignment_java_core.model.LogEntry;
import assignment_java_core.service.LogService;

import java.time.LocalDateTime;
import java.util.List;

public class LogController {
    private LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    public List<LogEntry> searchLogs(String level, LocalDateTime from, LocalDateTime to, String keyword){
        return logService.searchLogs(level, from, to, keyword);
    }

    public void exportLogs(List<LogEntry> logs){
        logService.exportLogs(logs);
    }
}
