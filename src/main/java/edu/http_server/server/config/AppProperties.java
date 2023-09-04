package edu.http_server.server.config;

import edu.http_server.server.di.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class AppProperties {
    private final Properties properties = new Properties();

    public AppProperties() {
        loadProperties("application.properties");
    }

    private void loadProperties(String filename) {
        try (InputStream is = AppProperties.class.getClassLoader().getResourceAsStream(filename)) {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}
