package assignment_java_core.service.impl;

import assignment_java_core.dto.request.LogRequest;
import assignment_java_core.dto.response.LogResponse;
import assignment_java_core.mapper.LogMapper;
import assignment_java_core.model.LogEntry;
import assignment_java_core.repository.LogRepository;
import assignment_java_core.repository.impl.LogRepositoryImpl;
import assignment_java_core.service.LogService;
import assignment_java_core.util.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogServiceImpl implements LogService {
    private LogRepository logRepository;

    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * Kiểm tra xem timestamp có nằm trong khoảng from-to không
     * Nếu from/to null thì coi như không giới hạn
     */
    public boolean isInRange(LocalDateTime timestamp, LocalDateTime from, LocalDateTime to) {
        if (timestamp == null) return false;
        if (from != null && timestamp.isBefore(from)) return false;
        if (to != null && timestamp.isAfter(to)) return false;
        return true;
    }

    @Override
    public boolean checkLog(LogEntry log, LogRequest logRequest) {
        // Lọc level
        if (logRequest.getLevelFilter() != null &&
                !log.getLevel()
                        .equalsIgnoreCase(logRequest.getLevelFilter())) return false;

        // Lọc khoảng thời gian
        if (!isInRange(log.getTimestamp(),
                logRequest.getFromTime(),
                logRequest.getToTime())) return false;

        // Lọc keyword trong message
        if (logRequest.getKeywordFilter() != null &&
                !log.getMessage().toLowerCase()
                        .contains(logRequest.getKeywordFilter().toLowerCase())) return false;

        return true;
    }

    @Override
    public List<LogResponse> searchLogs(LogRequest logRequest) {
        List<LogEntry> logs = logRepository.readAll();
        List<LogResponse> rs = new ArrayList<>();
        for(LogEntry log : logs){
            if(checkLog(log, logRequest)){
                rs.add(LogMapper.toResponse(log));
            }
        }
        return rs;
    }

    @Override
    public void exportLogs(List<LogResponse> logs) {
        String outputPaths = Utils.generateOutputFileName();
        logRepository.writeAll(logs, outputPaths);
    }
}
