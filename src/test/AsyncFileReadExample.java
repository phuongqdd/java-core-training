package test;

import assignment_java_core.model.LogEntry;
import assignment_java_core.util.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AsyncFileReadExample
        implements CompletionHandler<Integer, BlockingQueue<Boolean>> {

    private static final int BLOCKSIZE = 1024 * 1024; // đọc từng khối 1KB
    private static final String FILENAME = "search_result_20250901_223630.txt";

    private final ByteBuffer buffer = ByteBuffer.allocate(BLOCKSIZE);
    private AsynchronousFileChannel fileChannel;
    private long position = 0;

    // lưu kết quả
    private final List<LogEntry> entries = new ArrayList<>();
    // để ghép dòng bị cắt
    private final StringBuilder leftover = new StringBuilder();

    public static void main(String[] args) throws Exception {
        AsyncFileReadExample reader = new AsyncFileReadExample();
        List<LogEntry> result = reader.readLogFile();
        System.out.println("Tổng số log đọc được: " + result.size());
        result.forEach(System.out::println);
    }

    public List<LogEntry> readLogFile() throws IOException, InterruptedException {
        Path path = Paths.get(FILENAME);
        fileChannel = AsynchronousFileChannel.open(path);

        BlockingQueue<Boolean> done = new ArrayBlockingQueue<>(1);

        fileChannel.read(buffer, position, done, this);

        // chờ đến khi đọc xong
        done.take();

        return entries;
    }

    @Override
    public void completed(Integer result, BlockingQueue<Boolean> attachment) {
        if (result < 0) { // EOF
            // parse dòng cuối còn sót lại
            if (leftover.length() > 0) {
                parseLogLine(leftover.toString());
            }
            try {
                attachment.put(true); // báo hoàn thành
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        position += result;
        buffer.flip();
        String chunk = Charset.defaultCharset().decode(buffer).toString();
        buffer.clear();

        leftover.append(chunk);

        // tách theo dòng
        String[] lines = leftover.toString().split("\n");

        // parse toàn bộ dòng trọn vẹn
        for (int i = 0; i < lines.length - 1; i++) {
            parseLogLine(lines[i]);
        }

        // giữ lại dòng cuối (có thể bị cắt)
        leftover.setLength(0);
        leftover.append(lines[lines.length - 1]);

        // đọc tiếp
        fileChannel.read(buffer, position, attachment, this);
    }

    @Override
    public void failed(Throwable exc, BlockingQueue<Boolean> attachment) {
        try {
            attachment.put(false);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm parse log line
    private void parseLogLine(String line) {
        line = line.trim();
        if (line.isEmpty()) return;

        try {
            int tsStart = line.indexOf("[") + 1;
            int tsEnd = line.indexOf("]", tsStart);
            String timestamp = line.substring(tsStart, tsEnd);

            int levelStart = line.indexOf("[", tsEnd + 1) + 1;
            int levelEnd = line.indexOf("]", levelStart);
            String level = line.substring(levelStart, levelEnd);

            int svcStart = line.indexOf("[", levelEnd + 1) + 1;
            int svcEnd = line.indexOf("]", svcStart);
            String service = line.substring(svcStart, svcEnd);

            String message = line.substring(line.indexOf("-", svcEnd) + 2);

            entries.add(new LogEntry(Utils.parseTimestamp(timestamp), level, service, message));
        } catch (Exception e) {
            System.err.println("Không parse được dòng: " + line);
        }
    }
}
