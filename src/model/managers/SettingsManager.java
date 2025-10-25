package model.managers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SettingsManager {
    private Properties properties;
    private String filename = "config.properties";
    public static final String RENTAL_DURATION_KEY = "rental.duration";

    public SettingsManager() {
        properties = new Properties();
        loadSettings();
    }

    public void loadSettings() {
        try (FileInputStream fis = new FileInputStream(filename)) {
            properties.load(fis);
        } catch (IOException e) {
            // If file doesn't exist, set default
            properties.setProperty(RENTAL_DURATION_KEY, "7");
        }
    }

    public void saveSettings() {
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            properties.store(fos, "Library System Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSetting(String key) {
        return properties.getProperty(key);
    }

    public void setSetting(String key, String value) {
        properties.setProperty(key, value);
    }

    public int getRentalDuration() {
        return Integer.parseInt(getSetting(RENTAL_DURATION_KEY));
    }
}
