package com.github.dloiacono.ai.agents;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@RegisterAiService(modelName = "researcher")
public interface ResearcherAgent {

  @UserMessage(
      """
    Goal: Analyze and prepare a foundation for design.
    Instructions:
       - Read the user’s request carefully.
       - Research relevant technologies, frameworks, algorithms, and approaches.
       - Identify trade-offs, challenges, and recommended practices.
       - Do not propose detailed architecture or write code.
       - Output must be a structured Research Report containing:
       - Problem summary
       - Possible approaches
       - Recommended technologies
       - Key constraints
       - Risks and mitigations
       - References
       
    Here is the question: {query}
    """)
  @Tool("Calls the ResearcherAgent to perform research and analysis on a given topic or requirement")
  String chatWithResearcher(String query);
}
