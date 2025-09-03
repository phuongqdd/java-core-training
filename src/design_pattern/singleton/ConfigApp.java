package design_pattern.singleton;

public class ConfigApp {
    public static void main(String[] args) {
        ConfigManager config1 = ConfigManager.getInstance();

        System.out.println("App name: " + config1.getProperty("app.name"));

        ConfigManager config2 = ConfigManager.getInstance();
        System.out.println("App version: " + config2.getProperty("app.version"));

        System.out.println("Same instance? " + (config1 == config2));

        Runnable task = () -> {
            ConfigManagerThreadSafe config = ConfigManagerThreadSafe.getInstance();
            System.out.println(Thread.currentThread().getName() +
                    " - app.name = " + config.getProperty("app.name"));
        };

        Thread t1 = new Thread(task, "Thread-1");
        Thread t2 = new Thread(task, "Thread-2");
        Thread t3 = new Thread(task, "Thread-3");

        t1.start();
        t2.start();
        t3.start();
    }
}
