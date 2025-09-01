package assignment_java_core;

import assignment_java_core.controller.LogController;
import assignment_java_core.model.LogEntry;
import assignment_java_core.repository.LogRepository;
import assignment_java_core.repository.impl.LogRepositoryImpl;
import assignment_java_core.service.LogService;
import assignment_java_core.service.impl.LogServiceImpl;
import assignment_java_core.util.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String inputFile = "src/assignment_java_core/system_logs.log";
        String outputFile = "";

        LogRepository logRepository = new LogRepositoryImpl(inputFile);
        LogService logService = new LogServiceImpl(logRepository);
        LogController logController = new LogController(logService);
        LocalDateTime from = Utils.parseTimestamp("2024-07-13 00:00:00.000");
        LocalDateTime to   = Utils.parseTimestamp("2024-07-14 00:00:00.000");
        List<LogEntry> logs = new ArrayList<>();
        logs = logController.searchLogs("ERROR", from, to, " ");

        System.out.println("Tìm thâ: " + logs.size());
    }
}
