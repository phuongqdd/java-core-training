package assignment_java_core.service.impl;

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

    @Override
    public boolean checkLog(LogEntry log, String level, LocalDateTime from, LocalDateTime to, String keyword) {
        // Lọc level
        if (level != null && !log.getLevel().equalsIgnoreCase(level)) return false;

        // Lọc khoảng thời gian
        if (!Utils.isInRange(log.getTimestamp(), from, to)) return false;

        // Lọc keyword trong message
        if (keyword != null && !log.getMessage().toLowerCase().contains(keyword.toLowerCase())) return false;

        return true;
    }

    @Override
    public List<LogEntry> searchLogs(String level, LocalDateTime from, LocalDateTime to, String keyword) {
        List<LogEntry> logs = logRepository.readAll();
        List<LogEntry> rs = new ArrayList<>();
        for(LogEntry log : logs){
            if(checkLog(log, level, from, to, keyword)){
                rs.add(log);
            }
        }
        return rs;
    }

    @Override
    public void exportLogs(List<LogEntry> logs) {
        String outputPaths = Utils.generateOutputFileName();
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(outputPaths))) {
            for (LogEntry logEntry : logs){
                bw.write(logs.toString());
                bw.newLine();
            }
            System.out.println("Kết quả đã lưu vào file: " + outputPaths);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
