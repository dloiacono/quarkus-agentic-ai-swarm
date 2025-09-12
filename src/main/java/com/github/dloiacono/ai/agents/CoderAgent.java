package com.github.dloiacono.ai.agents;

import com.github.dloiacono.ai.agents.tools.ProjectContextTool;
import com.github.dloiacono.ai.agents.tools.ReadFileTool;
import com.github.dloiacono.ai.agents.tools.WriteFileTool;
import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

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
        - Loop until all tests passed
        - Writes content to a file in the filesystem, creating the file if it doesn't exist or overwriting it if it does
        - Appends content to a file in the filesystem, creating the file if it doesn't exist
        - Analyzes a project folder and provides comprehensive context about its structure, files, and content"
        - Use the ProjectContextTool to analyze project structure and understand existing codebase before making changes.
        - Use ReadFileTool and WriteFileTool for file operations on produced code.
    """)
    String chat(String query);

}
