package com.github.dloiacono.ai.agents.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Tool for writing content to files in the filesystem.
 */
@ApplicationScoped
public class WriteFileTool {

    /**
     * Writes the provided content to a file at the specified path.
     * Creates the file if it doesn't exist, or overwrites it if it does.
     *
     * @param content the content to write to the file
     * @param filePath the path to the file to write
     * @return a message indicating success or failure
     */
    @Tool("""
    Writes content to a file in the filesystem, 
    creating the file if it doesn't exist or overwriting it if it does.
    
    CRITICAL: BOTH parameters are MANDATORY and must never be null!
    - First parameter: content (the ACTUAL file content to write)
    - Second parameter: filePath (the file path)
    
    You MUST provide the complete file content as the first parameter.
    Do NOT call this tool without providing actual content to write.
    
    Example: writeFile("package com.example;\n\npublic class Example {\n    // code here\n}", "src/main/java/Example.java")
    """)
    public String writeFile(
            @P("MANDATORY content (string) - the ACTUAL file content to write - CANNOT be null - MUST contain the complete file content") String content,
            @P("MANDATORY filePath (string) - the RELATIVE full file path - CANNOT be null or empty") String filePath) {
        try {
            // Debug logging and null checks
            if (filePath == null || filePath.trim().isEmpty()) {
                return "Error: filePath parameter is null or empty";
            }
            if (content == null) {
                return "Error: content parameter is null. Please provide the actual file content to write.";
            }
            if (content.trim().isEmpty()) {
                return "Error: content parameter is empty. Please provide the actual file content to write - do not pass empty strings.";
            }
            
            // Convert to relative path if absolute path is provided
            String relativePath = convertToRelativePath(filePath);
            Path path = Paths.get(relativePath);
            
            // Create parent directories if they don't exist
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            
            // Write the content to the file
            Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return "Successfully wrote " + content.length() + " characters to file: " + relativePath;
        } catch (IOException e) {
            return "Error writing to file: " + e.getMessage();
        }
    }

    /**
     * Appends the provided content to a file at the specified path.
     * Creates the file if it doesn't exist.
     *
     * @param content the content to append to the file
     * @param filePath the path to the file to append to
     * @return a message indicating success or failure
     */
    @Tool("""
    Appends content to a file in the filesystem, creating the file if it doesn't exist.
    
    CRITICAL: BOTH parameters are MANDATORY and must never be null!
    - First parameter: content (the ACTUAL content to append)
    - Second parameter: filePath (the file path)
    
    Example: appendToFile("Some text to append\n", "src/main/java/Example.java")
    """)
    public String appendToFile(
            @P("MANDATORY content (string) - the ACTUAL content to append - CANNOT be null - MUST contain the actual content") String content,
            @P("MANDATORY filePath (string) - the RELATIVE full file path - CANNOT be null or empty") String filePath) {
        try {
            // Debug logging and null checks
            if (filePath == null || filePath.trim().isEmpty()) {
                return "Error: filePath parameter is null or empty";
            }
            if (content == null) {
                return "Error: content parameter is null. Please provide the actual content to append.";
            }
            
            // Convert to relative path if absolute path is provided
            String relativePath = convertToRelativePath(filePath);
            Path path = Paths.get(relativePath);
            
            // Create parent directories if they don't exist
            Path parent = path.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }
            
            // Append the content to the file
            Files.writeString(path, content, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            return "Successfully appended " + content.length() + " characters to file: " + relativePath;
        } catch (IOException e) {
            return "Error appending to file: " + e.getMessage();
        }
    }

    /**
     * Converts an absolute path to a relative path.
     *
     * @param filePath the original file path (absolute or relative)
     * @return a relative path
     */
    private String convertToRelativePath(String filePath) {
        if (filePath == null) {
            return null;
        }
        
        // If it's already a relative path, return as-is
        if (!filePath.startsWith("/") && !filePath.matches("^[A-Za-z]:\\\\.*")) {
            return filePath;
        }

        return System.getProperty("user.dir") + "/target/run-test/" + filePath;
    }
}
