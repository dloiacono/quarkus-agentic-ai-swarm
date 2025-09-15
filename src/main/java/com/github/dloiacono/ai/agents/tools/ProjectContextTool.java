package com.github.dloiacono.ai.agents.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Tool for analyzing project folders and providing context about their structure and content.
 */
@ApplicationScoped
public class ProjectContextTool {

    private static final Set<String> CODE_EXTENSIONS = Set.of(
        ".java", ".py", ".js", ".ts", ".jsx", ".tsx", ".go", ".rs", ".cpp", ".c", ".h",
        ".cs", ".php", ".rb", ".kt", ".scala", ".clj", ".hs", ".ml", ".fs", ".swift"
    );

    private static final Set<String> CONFIG_EXTENSIONS = Set.of(
        ".xml", ".json", ".yaml", ".yml", ".properties", ".toml", ".ini", ".conf"
    );

    private static final Set<String> DOC_EXTENSIONS = Set.of(
        ".md", ".txt", ".rst", ".adoc", ".org"
    );

    /**
     * Analyzes a project folder and returns a comprehensive overview of its structure and content.
     *
     * @param projectPath the path to the project folder
     * @return a detailed analysis of the project structure
     */
    @Tool("Analyzes a project folder and provides comprehensive context about its structure, files, and content")
    public String analyzeProject(
            @P("MANDATORY projectPath (string) - the RELATIVE full project path") String projectPath) {
        try {
            Path path = Paths.get(projectPath);
            if (!Files.exists(path) || !Files.isDirectory(path)) {
                return "Error: Invalid project path or not a directory: " + projectPath;
            }

            StringBuilder analysis = new StringBuilder();
            analysis.append("=== PROJECT ANALYSIS ===\n");
            analysis.append("Project Path: ").append(projectPath).append("\n\n");

            // Get project structure
            analysis.append("=== PROJECT STRUCTURE ===\n");
            analysis.append(getProjectStructure(path));
            analysis.append("\n");

            // Get file statistics
            analysis.append("=== FILE STATISTICS ===\n");
            analysis.append(getFileStatistics(path));
            analysis.append("\n");

            // Get key files content
            analysis.append("=== KEY FILES CONTENT ===\n");
            analysis.append(getKeyFilesContent(path));

            return analysis.toString();

        } catch (IOException e) {
            return "Error analyzing project: " + e.getMessage();
        }
    }

    /**
     * Gets the content of specific files in the project.
     *
     * @param projectPath the path to the project folder
     * @param filePattern glob pattern to match files (e.g., "**//*.java", "pom.xml", "package.json")
     * @return the content of matching files
     */
    @Tool("Gets the content of specific files in a project based on a glob pattern")
    public String getProjectFiles(String projectPath, String filePattern) {
        try {
            Path path = Paths.get(projectPath);
            if (!Files.exists(path) || !Files.isDirectory(path)) {
                return "Error: Invalid project path or not a directory: " + projectPath;
            }

            StringBuilder content = new StringBuilder();
            content.append("=== FILES MATCHING PATTERN: ").append(filePattern).append(" ===\n\n");

            PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + filePattern);
            
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path relativePath = path.relativize(file);
                    if (matcher.matches(relativePath) || matcher.matches(file.getFileName())) {
                        content.append("--- ").append(relativePath).append(" ---\n");
                        try {
                            String fileContent = Files.readString(file);
                            content.append(fileContent).append("\n\n");
                        } catch (IOException e) {
                            content.append("Error reading file: ").append(e.getMessage()).append("\n\n");
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });

            return content.toString();

        } catch (IOException e) {
            return "Error getting project files: " + e.getMessage();
        }
    }

    private String getProjectStructure(Path projectPath) throws IOException {
        StringBuilder structure = new StringBuilder();
        
        Files.walkFileTree(projectPath, EnumSet.noneOf(FileVisitOption.class), 3, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                Path relativePath = projectPath.relativize(dir);
                if (!relativePath.toString().isEmpty()) {
                    int depth = relativePath.getNameCount() - 1;
                    structure.append("  ".repeat(depth))
                            .append("📁 ")
                            .append(dir.getFileName())
                            .append("/\n");
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                Path relativePath = projectPath.relativize(file);
                int depth = relativePath.getNameCount() - 1;
                String extension = getFileExtension(file.getFileName().toString());
                String icon = getFileIcon(extension);
                
                structure.append("  ".repeat(depth))
                        .append(icon)
                        .append(" ")
                        .append(file.getFileName())
                        .append("\n");
                return FileVisitResult.CONTINUE;
            }
        });

        return structure.toString();
    }

    private String getFileStatistics(Path projectPath) throws IOException {
        Map<String, Integer> extensionCounts = new HashMap<>();
        Map<String, Long> extensionSizes = new HashMap<>();
        
        Files.walkFileTree(projectPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String extension = getFileExtension(file.getFileName().toString());
                extensionCounts.merge(extension, 1, Integer::sum);
                extensionSizes.merge(extension, attrs.size(), Long::sum);
                return FileVisitResult.CONTINUE;
            }
        });

        StringBuilder stats = new StringBuilder();
        stats.append("File Types:\n");
        
        extensionCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> {
                    String ext = entry.getKey();
                    int count = entry.getValue();
                    long size = extensionSizes.get(ext);
                    stats.append(String.format("  %s: %d files (%.1f KB)\n", 
                            ext, count, size / 1024.0));
                });

        return stats.toString();
    }

    private String getKeyFilesContent(Path projectPath) throws IOException {
        StringBuilder content = new StringBuilder();
        
        // Look for common project files
        String[] keyFiles = {
            "README.md", "README.txt", "pom.xml", "build.gradle", "package.json",
            "Cargo.toml", "requirements.txt", "setup.py", "Dockerfile", ".gitignore"
        };

        for (String keyFile : keyFiles) {
            Path filePath = projectPath.resolve(keyFile);
            if (Files.exists(filePath)) {
                content.append("--- ").append(keyFile).append(" ---\n");
                try {
                    String fileContent = Files.readString(filePath);
                    // Limit content length for key files
                    if (fileContent.length() > 2000) {
                        fileContent = fileContent.substring(0, 2000) + "\n... (truncated)";
                    }
                    content.append(fileContent).append("\n\n");
                } catch (IOException e) {
                    content.append("Error reading file: ").append(e.getMessage()).append("\n\n");
                }
            }
        }

        return content.toString();
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot) : "no extension";
    }

    private String getFileIcon(String extension) {
        if (CODE_EXTENSIONS.contains(extension)) {
            return "📄";
        } else if (CONFIG_EXTENSIONS.contains(extension)) {
            return "⚙️";
        } else if (DOC_EXTENSIONS.contains(extension)) {
            return "📝";
        } else {
            return "📄";
        }
    }
}
