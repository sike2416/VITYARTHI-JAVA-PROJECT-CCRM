package edu.ccrm.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppConfig {
    private static AppConfig instance;
    private Path dataFolderPath;
    private Path backupFolderPath;
    private int maxCreditsPerSemester;

    // Private constructor to prevent instantiation
    private AppConfig() {
        // Default configuration
        this.dataFolderPath = Paths.get("data");
        this.backupFolderPath = Paths.get("backup");
        this.maxCreditsPerSemester = 21;
    }

    // Static method to get the singleton instance
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }

    // Getters and setters
    public Path getDataFolderPath() { return dataFolderPath; }
    public void setDataFolderPath(Path dataFolderPath) { this.dataFolderPath = dataFolderPath; }
    public Path getBackupFolderPath() { return backupFolderPath; }
    public void setBackupFolderPath(Path backupFolderPath) { this.backupFolderPath = backupFolderPath; }
    public int getMaxCreditsPerSemester() { return maxCreditsPerSemester; }
    public void setMaxCreditsPerSemester(int maxCreditsPerSemester) { this.maxCreditsPerSemester = maxCreditsPerSemester; }

    // Method to load configuration
    public void loadConfig() {
        System.out.println("Configuration loaded with data folder: " + dataFolderPath);
        System.out.println("Configuration loaded with backup folder: " + backupFolderPath);
    }
}