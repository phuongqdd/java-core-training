package assignment_java_core.repository;

import assignment_java_core.model.LogEntry;

import java.util.List;

public interface LogRepository {
    List<LogEntry> readAll();
}
