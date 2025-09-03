package design_pattern.singleton;

/**
 * - Mục đích:
 *      + Tạo sẵn instance ngay khi class được lòa vào bộ nhớ
 * - Ưu điểm:
 *      + Đơn giản, dễ hiểu
 *      + Thread-safe tự nhiên (do JVM đảm bảo class loading)
 * - Nhược điểm:
 *      + Tốn tài nguyên nếu instance không được dùng
 *      + Không linh hoạt (luôn tạo từ đầu)
 * - Ví dụ sử dụng:
 *      + Constant config: các thông số câu hình chung cần sẵn sàng kh chương trình chạy
 * - Trường hợp sử dụng
 *      + Khi chắc chắn 100% instance sẽ được dùng.
 *      + Khi instance nhẹ, không tốn nhiều tài nguyên.
 *      + Không quan tâm tới lazy loading
 */

public class EagerInitialization {
    private static final EagerInitialization INSTANCE = new EagerInitialization();

    private EagerInitialization(){
        System.out.println("Eager khởi tạo...");
    }

    public static EagerInitialization getInstance(){
        return INSTANCE;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
