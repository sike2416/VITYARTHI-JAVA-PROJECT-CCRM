package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.stream.Stream;

public class RecursiveUtils {

    /**
     * Recursively calculates the total size of a directory in bytes
     */
    public static long calculateDirectorySize(Path directory) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) return 0;

        try (Stream<Path> stream = Files.walk(directory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .mapToLong(path -> {
                        try {
                            return Files.size(path);
                        } catch (IOException e) {
                            return 0;
                        }
                    })
                    .sum();
        }
    }

    /**
     * Recursively lists files by depth
     */
    public static void listFilesByDepth(Path directory, int maxDepth) throws IOException {
        if (directory == null || !Files.isDirectory(directory)) {
            System.out.println("Invalid directory: " + directory);
            return;
        }

        if (maxDepth < 0) maxDepth = Integer.MAX_VALUE;

        System.out.println("Listing files in: " + directory + " (max depth: " + maxDepth + ")");

        try (Stream<Path> stream = Files.walk(directory, maxDepth)) {
            stream.forEach(path -> {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
                    int depth = directory.relativize(path).getNameCount();

                    String indent = "  ".repeat(depth);

                    if (attrs.isDirectory()) {
                        System.out.println(indent + "[DIR] " + path.getFileName());
                    } else {
                        System.out.println(indent + "[FILE] " + path.getFileName() +
                                " (" + attrs.size() + " bytes)");
                    }
                } catch (IOException e) {
                    System.out.println("Error reading attributes for: " + path);
                }
            });
        }
    }
}