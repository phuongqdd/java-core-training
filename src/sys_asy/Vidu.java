package sys_asy;

// Một ngân hàng có nhiều khách hàng (thread) cùng rút tiền từ tài khoản chung.
// Nếu không đồng bộ hóa, có thể xảy ra tình trạng race condition → số dư âm.
class BankAccount {
    private int balance = 1000; // số dư ban đầu

    // synchronized để đảm bảo 1 lúc chỉ 1 thread có thể rút tiền
    public synchronized void withdraw(int amount, String name) {
        if (balance >= amount) {
            System.out.println(name + " đang rút " + amount + "...");
            balance -= amount;
            System.out.println(name + " đã rút thành công. Số dư còn lại: " + balance);
        } else {
            System.out.println(name + " không thể rút " + amount + ". Số dư không đủ!");
        }
    }
}

public class Vidu {
    public static void main(String[] args) {
        BankAccount account = new BankAccount();

        // Tạo nhiều luồng (khách hàng) cùng rút tiền
        Thread t1 = new Thread(() -> account.withdraw(700, "Khách hàng A"));
        Thread t2 = new Thread(() -> account.withdraw(500, "Khách hàng B"));
        Thread t3 = new Thread(() -> account.withdraw(300, "Khách hàng C"));

        t1.start();
        t2.start();
        t3.start();
    }
}
