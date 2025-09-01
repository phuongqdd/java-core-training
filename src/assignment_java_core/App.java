package assignment_java_core;

import assignment_java_core.controller.LogController;
import assignment_java_core.dto.request.LogRequest;
import assignment_java_core.dto.response.LogResponse;
import assignment_java_core.model.LogEntry;
import assignment_java_core.repository.LogRepository;
import assignment_java_core.repository.impl.LogRepositoryImpl;
import assignment_java_core.service.LogService;
import assignment_java_core.service.impl.LogServiceImpl;
import assignment_java_core.util.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        String inputFile = "src/assignment_java_core/system_logs.log";

        LogRepository logRepository = new LogRepositoryImpl(inputFile);
        LogService logService = new LogServiceImpl(logRepository);
        LogController logController = new LogController(logService);

        LogRequest logRequest = logController.inputRequest();

        List<LogResponse> logs = logController.searchLogs(logRequest);

        System.out.println("Tìm thấy: " + logs.size() + " kết quả\nBạn có mốn xuất kết quả không!");
        boolean choice = logController.confirmExport();
        if(choice){
            logController.exportLogs(logs);
        }
    }
}
