package assignment_java_core.repository.impl;

import assignment_java_core.dto.response.LogResponse;
import assignment_java_core.model.LogEntry;
import assignment_java_core.repository.LogRepository;
import assignment_java_core.util.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogRepositoryImpl implements LogRepository {
    private final String filePath;

    public LogRepositoryImpl(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<LogEntry> readAll() {
        List<LogEntry> logs = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
            String line;
            while ((line = br.readLine()) != null){
                if(line.trim().isEmpty()) continue;

                logs.add(Utils.parseLogLine(line));
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return logs;
    }

    @Override
    public void writeAll(List<LogResponse> logs, String outputPaths) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(outputPaths))) {
            for (LogResponse logResponse : logs){
                bw.write(logResponse.toString());
                bw.newLine();
            }
            System.out.println("Kết quả đã lưu vào file: " + outputPaths);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
