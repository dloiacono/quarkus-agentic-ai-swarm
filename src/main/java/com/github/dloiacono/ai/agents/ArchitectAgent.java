package com.github.dloiacono.ai.agents;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService(modelName = "architect")
public interface ArchitectAgent {

    @UserMessage("""
    Goal: Translate research into a software design.
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
        
    Here is the question: {query}
    """)
    @Tool("Calls the ArchitectAgent to design system architecture based on requirements and research")
    String chatWithArchitect(String query);

}
