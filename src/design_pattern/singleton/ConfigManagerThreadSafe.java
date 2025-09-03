package design_pattern.singleton;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManagerThreadSafe {
    private static ConfigManagerThreadSafe instance;
    private Properties properties;

    private ConfigManagerThreadSafe(){
        properties = new Properties();
        try(FileInputStream fis = new FileInputStream("src/design_pattern/singleton/config.properties")) {
            properties.load(fis);
            System.out.println("Config loaded by " + Thread.currentThread().getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized ConfigManagerThreadSafe getInstance(){
        if(instance == null){
            instance = new ConfigManagerThreadSafe();
        }
        return instance;
    };

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}
