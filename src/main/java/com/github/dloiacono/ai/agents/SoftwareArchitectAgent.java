package com.github.dloiacono.ai.agents;

import com.github.dloiacono.ai.tools.WriteFileTool;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "architect", tools = {WriteFileTool.class})
public interface SoftwareArchitectAgent {

  @SystemMessage(
      """
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
        - Output MUST be a structured Architecture Specifications.

   OUTPUTS:
        - you MUST produce always MD files under architecture folder, that contains your results.
        
   Available Tools:
        - Use WriteFileTool to create new files or modify existing files.
    """)
  @UserMessage(
    """
        You convert the requirements into a architecture specification and give specifications to the code agent.
        
        The requirements are: '{requirements}'.
        You produce a Architecture Specifications.
    """)
  String invoke(@V("requirements") String requirements);
}
