package com.github.dloiacono.ai.agents;

import com.github.dloiacono.ai.tools.WriteFileTool;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(modelName = "researcher", tools = {WriteFileTool.class})
public interface ResearcherAgent {

  @SystemMessage(
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
   
   OUTPUTS:
        - produce always MD files under research folder, that contains your results.
   
   Available Tools:
        - Use WriteFileTool to create new files or modify existing files.
    """)
  @UserMessage(
    """
    You convert the user needs into a research report and return list of requirements for the architect agent
    
    The user needs are: '{needs}'.
    """)
  String invoke(@V("needs") String needs);
}
