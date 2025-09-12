package com.github.dloiacono.ai.agents;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "coder")
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
    """)
    String chat(String query);

}
