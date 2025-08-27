package sys_asy;

import java.util.concurrent.CompletableFuture;

public class Cau1 {

    // Synchronous
    public static  void loadData(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // Synchronous (Đồng bộ)
        // Mỗi tác vụ (task) sẽ được thực hiện theo thứ tự tuần tự
        // Tác vụ sau chỉ chạy khi tác vụ trước đã hoàn thành
        // Ưu điểm: dễ hiểu, dễ debug, logic rõ ràng
        // Nhược điểm: có thể gây chặn(blocking), làm hệ thống chậm nếu một tác vụ ất nhiều thời gian
//        System.out.println("Bắt đầu tải dữ liệu...");
//        loadData();
//        System.out.println("Dữ liệu đã tải xong!");


        // Asynchronous(Bất đồng bộ)
        // Tác vụ có thể được thực thi song song/không chặn
        // Khi một tác vụ đang chạy, tác vụ khác vẫn có thể tiếp tục mà không cần chờ
        // Ưu điểm: tận dụng tài nguyên tốt, hệ thống phản hồi nhanh
        // Nhược điểm: Phức tạp hơn để code & debug, dễ gặp lỗi race condition

        System.out.println("Bắt đầu tải dữ liệu ...");
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Dữ lệu đã tải xong!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println("Trong lúc chờ dữ liệu, làm việc khác...");
        future.join();
    }
}
