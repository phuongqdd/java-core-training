package assignment_java_core.repository.impl;

import assignment_java_core.model.LogEntry;
import assignment_java_core.repository.LogRepository;
import assignment_java_core.util.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
}
