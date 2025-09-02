package assignment_java_core.repository.impl;

import assignment_java_core.dto.response.LogResponse;
import assignment_java_core.model.LogEntry;
import assignment_java_core.repository.AsyncLogReader;
import assignment_java_core.repository.LogRepository;
import assignment_java_core.util.Utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class LogRepositoryImpl implements LogRepository {
    private final String filePath;
    private AsyncLogReader reader;
    public LogRepositoryImpl(String filePath) {
        this.filePath = filePath;
        reader = new AsyncLogReader(this.filePath);
    }

//    @Override
//    public List<LogEntry> readAll() {
//        List<LogEntry> logs = new ArrayList<>();
//        try(BufferedReader br = new BufferedReader(new FileReader(filePath))){
//            String line;
//            while ((line = br.readLine()) != null){
//                if(line.trim().isEmpty()) continue;
//
//                logs.add(Utils.parseLogLine(line));
//            }
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println("Đọc được: " + logs.size() + " bản ghi!");
//        return logs;
//    }

    @Override
    public List<LogEntry> readAll(){
        try {
            List<LogEntry> rs = this.reader.readLogFile();
            return rs;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
