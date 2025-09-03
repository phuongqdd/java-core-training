package design_pattern.singleton;

public class ClientTest {
    public static void main(String[] args) {
        EagerInitialization eager1 = EagerInitialization.getInstance();

        eager1.setName("Phúc");
        System.out.println(eager1.getName());

        EagerInitialization eager2 = EagerInitialization.getInstance();

        System.out.println(eager2.getName());

//        LazyInitialization lazy1 = LazyInitialization.getInstance();
//
//        lazy1.setKey("abc");
//        lazy1.setValue("123");
//        System.out.println(lazy1.toString());
//
//        LazyInitialization lazy2 = LazyInitialization.getInstance();
//        System.out.println(lazy1.toString());
        // Trường hợp đa luồng với lazy
//        Runnable task = () -> {
//          LazyInitialization singleton = LazyInitialization.getInstance();
//          System.out.println("Got instance: " + singleton.hashCode() + " from " + Thread.currentThread().getName());
//        };
//
//        Thread t1 = new Thread(task, "Thread-1");
//        Thread t2 = new Thread(task, "Thread-2");
//        Thread t3 = new Thread(task, "Thread-3");
//
//        t1.start();
//        t2.start();
//        t3.start();

        //Thread Safe
//        Runnable task1 = () -> {
//          ThreadSafeInitialization thread = ThreadSafeInitialization.getInstance();
//          thread.log("Hello from " + Thread.currentThread().getName());
//        };
//
//        new Thread(task1, "Thread-1").start();
//        new Thread(task1, "Thread-2").start();

        //Nhuoc diem cua Thread Safe
//        Runnable task = () -> {
//            long start = System.currentTimeMillis();
//            for (int i = 0; i < 10000; i++) {
//                ThreadSafeInitialization.getInstance();
//            }
//            long end = System.currentTimeMillis();
//            System.out.println(Thread.currentThread().getName() + " mất " + (end - start) + " ms");
//        };

        //Với Double check
        Runnable task = () -> {
            long start = System.currentTimeMillis();
            for (int i = 0; i < 10000; i++) {
                DoubleCheckedLocking.getInstance();
            }
            long end = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName() + " mất " + (end - start) + " ms");
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");

        t1.start();
        t2.start();
        t3.start();
    }
}
