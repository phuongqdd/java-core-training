package design_pattern.singleton;

/**
 * Mục đích:
 *      Tối ưu hiệu năng trong môi trường đa luồng.
 * Ưu điểm:
 *      Thread-safe.
 *      Chỉ synchronized khi cần (lần khởi tạo đầu tiên).
 *      Hiệu năng tốt hơn synchronized toàn method.
 * Nhược điểm
 *      Code phức tạp hơn, dễ sai nếu không dùng volatile.
 * Ví dụ sử dụng:
 *      Database Connection Pool: chỉ tạo một lần khi có kết nối đầu tiên, nhưng phải an toàn đa luồng.
 * Trường hợp sử dụng
 *      Khi ứng dụng đa luồng cần hiệu năng cao.
 *      Khi getInstance() được gọi thường xuyên → tránh synchronized toàn method.
 */

public class DoubleCheckedLocking {
    private static volatile DoubleCheckedLocking instance;

    private DoubleCheckedLocking(){
        System.out.println("ConnectionPool created..." + Thread.currentThread().getName());
    }

    public static DoubleCheckedLocking getInstance(){
        if(instance == null){
            synchronized (DoubleCheckedLocking.class){
                if(instance == null){
                    instance = new DoubleCheckedLocking();
                }
            }
        }
        return instance;
    }
}
