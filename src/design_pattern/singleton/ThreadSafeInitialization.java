package design_pattern.singleton;

/**
 * - Mục đích:
 *      + Đảm bảo chỉ 1 instance được ta trong môi trường đa luồng
 * - Ưu điểm:
 *      + Đơn giản, an toàn với đa luồng
 * - Nhược điểm:
 *      + Gọi getInstance() nhiều lần sẽ chậm do synchronized khóa toàn bộ method
 * - Ví dụ sử dụng:
 *      + Logger hệ thống: thường nhiều thread ghi log cùng lúc, cần thread-safe
 *
 * - Trường hợp sử dụng:
 *      + Khi chạy trong môi trường đa luồng.
 *      + Khi số lần gọi getInstance() không quá nhiều (vì synchronized làm giảm hiệu năng).
 *      + Phù hợp khi instance tạo xong rồi thì ít gọi lại.
 */
public class ThreadSafeInitialization {
    private static volatile ThreadSafeInitialization instance;

    private ThreadSafeInitialization(){
        System.out.println("Logger initialized ... bởi " + Thread.currentThread().getName());
    }

    public static synchronized ThreadSafeInitialization getInstance(){
        if(instance == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            instance = new ThreadSafeInitialization();
        }
        return instance;
    }

    public void log(String message){
        System.out.println("[LOG] " + message);
    }


}
