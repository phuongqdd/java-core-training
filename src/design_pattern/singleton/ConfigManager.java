package design_pattern.singleton;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static ConfigManager instance;
    private Properties properties;

    private ConfigManager(){
        properties = new Properties();
        try(FileInputStream fis = new FileInputStream("src/design_pattern/singleton/config.properties")) {
            properties.load(fis);
            System.out.println("Config loaded by " + Thread.currentThread().getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigManager getInstance(){
        if(instance == null){
            instance = new ConfigManager();
        }
        return  instance;
    };

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}
