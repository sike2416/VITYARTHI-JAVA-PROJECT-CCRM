package edu.ccrm.io;

import edu.ccrm.config.AppConfig;
import edu.ccrm.util.RecursiveUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupService {
    private final ImportExportService importExportService;
    private final AppConfig config;

    public BackupService(ImportExportService importExportService, AppConfig config) {
        this.importExportService = importExportService;
        this.config = config;
    }

    public Path createBackup() throws IOException {
        // Create timestamp for backup folder
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(formatter);

        // Create backup directory
        Path backupDir = config.getBackupFolderPath().resolve("backup_" + timestamp);
        Files.createDirectories(backupDir);

        // Export all data to backup directory
        importExportService.exportAllData(backupDir);

        System.out.println("Backup created at: " + backupDir);
        return backupDir;
    }

    public long getBackupSize() throws IOException {
        Path backupDir = config.getBackupFolderPath();
        if (!Files.exists(backupDir)) return 0;

        return RecursiveUtils.calculateDirectorySize(backupDir);
    }

    public void listBackupContents(int maxDepth) throws IOException {
        Path backupDir = config.getBackupFolderPath();
        if (!Files.exists(backupDir)) {
            System.out.println("Backup directory does not exist: " + backupDir);
            return;
        }

        RecursiveUtils.listFilesByDepth(backupDir, maxDepth);
    }
}