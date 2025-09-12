package com.github.dloiacono.ai.agents.tools;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Tool for reading files from the filesystem.
 */
@ApplicationScoped
public class ReadFileTool {

    /**
     * Reads the content of a file at the specified path.
     *
     * @param filePath the path to the file to read
     * @return the content of the file as a string
     */
    @Tool("Reads a file from the filesystem and returns its content as a string")
    public String readFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                return "Error: File not found at path: " + filePath;
            }
            if (!Files.isReadable(path)) {
                return "Error: File is not readable: " + filePath;
            }
            return Files.readString(path);
        } catch (IOException e) {
            return "Error reading file: " + e.getMessage();
        }
    }
}
