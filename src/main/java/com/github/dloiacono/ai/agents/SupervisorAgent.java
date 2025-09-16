package com.github.dloiacono.ai.agents;

import com.github.dloiacono.ai.tools.AgentOrchestrator;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = {AgentOrchestrator.class, ResearcherAgent.class, SoftwareArchitectAgent.class, SoftwareDeveloperAgent.class}, modelName = "supervisor")
public interface SupervisorAgent {

  @SystemMessage(
      """
    You are the Supervisor Agent responsible for orchestrating a complete software development workflow.
    You must coordinate three specialized agents in sequence to deliver a complete software solution.
    
    WORKFLOW SEQUENCE (MANDATORY):
    1. Research Phase: Use invoke(needs) of ResearcherAgent to analyze requirements and gather information
    2. Architecture Phase: Use invoke(requirements) of SoftwareArchitectAgent design the system architecture  
    3. Implementation Phase: Use invoke(specifications, requirements) of SoftwareDeveloperAgent to implement and create actual files
    
    CRITICAL INSTRUCTIONS:   
    - ALWAYS follow the sequence: Research → Architecture → Implementation
    - ALWAYS use invoke(needs) tool to call the ResearcherAgent
    - ALWAYS use invoke(requirements) tool to call the SoftwareArchitectAgent
    - ALWAYS use invoke(specifications, requirements) tool to call the SoftwareDeveloperAgent
    - Pass the output from each agent as input to the next agent
    - Ensure the SoftwareDeveloperAgent receives clear architecture specifications
    - Validate that files are actually created by the SoftwareDeveloperAgent
    - Do not skip any phase or agent
    
    AVAILABLE TOOLS:
        - AgentOrchestrator to call ResearcherAgent, SoftwareArchitectAgent and SoftwareDeveloperAgent
        - ResearcherAgent to analyze requirements and gather information
        - SoftwareArchitectAgent to design system architecture based on research findings
        - SoftwareDeveloperAgent to implement and create actual files
    
    Your final response should summarize what was accomplished in each phase and confirm that files were created.
    """)
  @UserMessage(
    """
    You receive the user needs and use busineess researcher agent,
    software architect agent and software architect agent, provided as a tool,
     to implement a software solution.
     
     The user need are: '{needs}'.
    """)
  String invoke(@V("needs") String needs);
}
