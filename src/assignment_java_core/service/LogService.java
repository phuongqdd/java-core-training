package assignment_java_core.service;

import assignment_java_core.dto.request.LogRequest;
import assignment_java_core.dto.response.LogResponse;
import assignment_java_core.model.LogEntry;

import java.time.LocalDateTime;
import java.util.List;

public interface LogService {
    boolean checkLog(LogEntry log, LogRequest logRequest);

    List<LogResponse> searchLogs(LogRequest logRequest);

    void exportLogs(List<LogResponse> logs);
}
