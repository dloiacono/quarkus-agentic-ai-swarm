package com.github.dloiacono.ai.agents.tools;

import com.github.dloiacono.ai.agents.ArchitectAgent;
import com.github.dloiacono.ai.agents.CoderAgent;
import com.github.dloiacono.ai.agents.ResearcherAgent;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AgentOrchestrator {

    @Inject
    ResearcherAgent researcherAgent;

    @Inject
    ArchitectAgent architectAgent;

    @Inject
    CoderAgent coderAgent;

    @Tool("Call the ResearcherAgent to conduct research on requirements, technologies, and best practices")
    public String callResearcher(@P("The research query or requirements to investigate") String query) {
        try {
            return researcherAgent.chatWithResearcher(query);
        } catch (Exception e) {
            return "Error calling ResearcherAgent: " + e.getMessage();
        }
    }

    @Tool("Call the ArchitectAgent to design system architecture based on research findings")
    public String callArchitect(@P("The research findings and requirements to design architecture for") String requirements) {
        try {
            return architectAgent.chatWithArchitect(requirements);
        } catch (Exception e) {
            return "Error calling ArchitectAgent: " + e.getMessage();
        }
    }

    @Tool("Call the CoderAgent to implement the designed solution by writing actual code files")
    public String callCoder(@P("The architecture specification and implementation requirements") String architectureSpec) {
        try {
            return coderAgent.chatWithCoder(architectureSpec);
        } catch (Exception e) {
            return "Error calling CoderAgent: " + e.getMessage();
        }
    }
}
