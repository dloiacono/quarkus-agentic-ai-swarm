package com.github.dloiacono.ai.agents;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "architect")
public interface ArchitectAgent {

    @SystemMessage("""
    Goal: Design a comprehensive system architecture.
    Instructions:
        - Use the user’s request + Research Report as input.
        - Design a complete Architecture Specification, including:
        - High-level system overview
        - Chosen technologies (with justification)
        - Components/modules and their responsibilities
        - Data models, APIs, and integrations
        - Deployment and scalability considerations
        - Security considerations
        - Do not write implementation code.
        - Output must be a structured Architecture Specification.
        
    """)
    String chatWithArchitect(String query);

}
