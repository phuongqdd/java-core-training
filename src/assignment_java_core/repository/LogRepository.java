package assignment_java_core.repository;

import assignment_java_core.dto.response.LogResponse;
import assignment_java_core.model.LogEntry;

import java.util.List;

public interface LogRepository {
    List<LogEntry> readAll();
    void writeAll(List<LogResponse> logs, String outputPaths);
}
