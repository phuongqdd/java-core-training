package assignment_java_core.mapper;

import assignment_java_core.dto.response.LogResponse;
import assignment_java_core.model.LogEntry;

public class LogMapper {
    public static LogResponse toResponse(LogEntry entry) {
        return new LogResponse(
                entry.getTimestamp(),
                entry.getLevel(),
                entry.getService(),
                entry.getMessage()
        );
    }

}
