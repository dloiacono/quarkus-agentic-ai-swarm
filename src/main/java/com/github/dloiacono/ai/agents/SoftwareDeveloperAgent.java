package com.github.dloiacono.ai.agents;

import com.github.dloiacono.ai.tools.ProjectContextTool;
import com.github.dloiacono.ai.tools.ReadFileTool;
import com.github.dloiacono.ai.tools.WriteFileTool;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "coder", tools = {ReadFileTool.class, WriteFileTool.class, ProjectContextTool.class})
public interface SoftwareDeveloperAgent {

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
        - Document everithings you creates
        
    CRITICAL FILE WRITING RULES:
        - You MUST use the available tools to create actual files:
        - ALWAYS use ProjectContextTool to analyze the project structure before making changes.
        - ALWAYS use WriteFileTool to create new files or modify existing files.
        - ALWAYS use ReadFileTool to read existing files when needed.
        - ALWAYS use relative file paths.
        - DO NOT just provide code snippets - you must write the actual files using WriteFileTool.
        - Create all necessary files including source code, tests, and configuration files.
        
    MANDATORY TOOL USAGE - READ THIS CAREFULLY:
        - The writeFile() function requires EXACTLY TWO parameters: (content, filePath)
        - NEVER call writeFile() with only one parameter
        - NEVER call writeFile() with null or empty content parameter
        - ALWAYS provide the complete file content as the FIRST parameter
        - The content parameter must contain the actual code/text to write
        - DO NOT use placeholders like "// implementation here" - provide real code
        - Example: writeFile("package com.example;\n\npublic class Example {\n    public void method() {\n        System.out.println(\"Hello\");\n    }\n}", "src/main/java/Example.java")
        
    Available Tools:
        - ProjectContextTool: analyzeProject(projectPath) and getProjectFiles(projectPath, pattern)
        - ReadFileTool: readFile(filePath) - reads file content
        - ReadFileTool: fileExists(filePath) - checks if file exists before reading (returns "true" or "false")
        - WriteFileTool: writeFile(content, filePath) - TWO parameters REQUIRED: complete file content FIRST, then file path
        - WriteFileTool: appendToFile(content, filePath) - TWO parameters REQUIRED: content to append FIRST, then file path
        
    BEST PRACTICE: Use fileExists() to check if a file exists before calling readFile() to avoid errors. 
        
    """)
    @UserMessage(
    """
        You receive architecture specifications and requirements and write the code to filesystem.
        
        The architecture specifications are: '{specifications}'.
        The requirements are: '{requirements}'.
    """)
    String invoke(@V("specifications") String specifications, @V("requirements") String requirements);

}
