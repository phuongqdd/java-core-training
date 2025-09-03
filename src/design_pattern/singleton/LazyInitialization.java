package design_pattern.singleton;

/**
 * - Mục đích:
 *      + Chỉ tạo instance khi thật sự cần đến
 * - Ưu điểm:
 *      + Tiết kiệm tài nguyên (chỉ khởi tạo khi dùng)
 *      + Đơn gản hơn so với giải pháp đồng bộ
 * - Nhược điểm:
 *      + Không thread-safe -> nếu nhiều luồng gọi cùng lúc có thể tạo nhiều instance
 * - Ví dụ:
 *      + Cahe tạm thời: chỉ khi cần lưu trữ dữ liệu thì mới tạo instance
 * - Trường hợp sử dụng:
 *      + Khi instance nặng hoặc không chắc sẽ được dùng → tránh tạo sớm gây lãng phí.
 *      + Chỉ chạy trong môi trường đơn luồng (single-thread).
 */

public class LazyInitialization {
    private static LazyInitialization instance;

    private LazyInitialization(){
        System.out.println("Lazy khởi tạo... bởi " + Thread.currentThread().getName());
    }

    public static LazyInitialization getInstance(){
        if(instance == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            instance = new LazyInitialization();
        }
        return instance;
    }

    private String key;
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "LazyInitialization{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
