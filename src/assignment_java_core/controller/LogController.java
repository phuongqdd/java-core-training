package assignment_java_core.controller;

import assignment_java_core.dto.request.LogRequest;
import assignment_java_core.dto.response.LogResponse;
import assignment_java_core.model.LogEntry;
import assignment_java_core.service.LogService;
import assignment_java_core.util.Utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class LogController {
    private LogService logService;
    private Scanner sc;

    public LogController(LogService logService) {
        this.logService = logService;
        this.sc = new Scanner(System.in);
    }

    public LocalDateTime inputTime(){
        while (true){
            String str = this.sc.nextLine().trim();
            if(str.isEmpty()) return null;

            try {
                return Utils.parseTimestamp(str);
            }catch (DateTimeParseException exception){
                System.out.print("Sai định dạng! Định dạng đúng: yyyy-MM-dd HH:mm:ss.SSS: ");
            }
        }
    }

    public LogRequest inputRequest() {
        String levelFilter = null, keywordFilter = null;
        LocalDateTime fromTime = null, toTime = null;

        while (true) {
            System.out.print("Nhập log level (INFO/WARN/ERROR) hoặc Enter bỏ qua: ");
            String level = this.sc.nextLine().trim();
            if (!level.isEmpty()) levelFilter = level;

            System.out.print("Nhập từ khóa trong message hoặc Enter bỏ qua: ");
            String keyword = this.sc.nextLine().trim();
            if (!keyword.isEmpty()) keywordFilter = keyword;

            System.out.print("Nhập thời gian từ (yyyy-MM-dd HH:mm:ss.SSS) hoặc Enter bỏ qua: ");
            fromTime = inputTime();

            System.out.print("Nhập thời gian đến (yyyy-MM-dd HH:mm:ss.SSS) hoặc Enter bỏ qua: ");
            toTime = inputTime();

            if (levelFilter != null || keywordFilter != null || fromTime != null || toTime != null) {
                break;
            } else {
                System.out.println("Bạn phải nhập ít nhất một tiêu chí tìm kiếm. Vui lòng thử lại.");
            }
        }

        return new LogRequest(levelFilter, keywordFilter, fromTime, toTime);
    }
    public List<LogResponse> searchLogs(LogRequest logRequest){
        return logService.searchLogs(logRequest);
    }

    public boolean confirmExport() {
        while (true) {
            String input = this.sc.nextLine().trim().toLowerCase();

            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("Vui lòng nhập 'y' hoặc 'n'.");
            }
        }
    }

    public void exportLogs(List<LogResponse> logs){
        logService.exportLogs(logs);
    }
}
