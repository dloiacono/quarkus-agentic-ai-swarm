package com.github.dloiacono.ai.agents;

import com.github.dloiacono.ai.agents.tools.ProjectContextTool;
import com.github.dloiacono.ai.agents.tools.ReadFileTool;
import com.github.dloiacono.ai.agents.tools.WriteFileTool;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@RegisterAiService(modelName = "coder", tools = {ReadFileTool.class, WriteFileTool.class, ProjectContextTool.class})
public interface CoderAgent {

    @SystemMessage("""
    Goal: Implement the designed solution.
    Instructions:
        - Use the user’s request + Architecture Specification as input. 
        - Write production-quality code that implements the design.
        - Follow best practices for readability, maintainability, testing, and security.
        - Include setup instructions, configuration notes, and usage examples.
        - If relevant, include unit tests or integration tests.
        - Output must be a complete Code Implementation.
        - Use Test Driven Development (TDD) to write tests first and then implement the code.
        - Loop until all tests passed.
        
    CRITICAL:
        - You MUST use the available tools to create actual files:
        - ALWAYS use ProjectContextTool to analyze the project structure before making changes.
        - ALWAYS use WriteFileTool to create new files or modify existing files.
        - ALWAYS use ReadFileTool to read existing files when needed.
        - ALWAYS send the content of the file to be created or modified to the WriteFileTool.
        - ALWAYS use relative file paths.
        - DO NOT just provide code snippets - you must write the actual files using WriteFileTool.
        - Create all necessary files including source code, tests, and configuration files.
        
    Available Tools:
        - ProjectContextTool: analyzeProject(projectPath) and getProjectFiles(projectPath, pattern)
        - ReadFileTool: readFile(filePath)
        - WriteFileTool: writeFile(filePath, content) and appendToFile(filePath, content). 
    Content of the file must always be passed to the write tool 
        
    Here is the question: {query}
    """)
    @Tool("Calls the CoderAgent to implement the solution based on architecture specifications")
    String chatWithCoder(String query);

}
