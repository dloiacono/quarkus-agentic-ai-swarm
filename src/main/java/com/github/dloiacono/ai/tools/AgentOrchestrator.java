package com.github.dloiacono.ai.tools;

import com.github.dloiacono.ai.agents.ResearcherAgent;
import com.github.dloiacono.ai.agents.SoftwareArchitectAgent;
import com.github.dloiacono.ai.agents.SoftwareDeveloperAgent;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AgentOrchestrator {

    @Inject
    ResearcherAgent researcherAgent;

    @Inject
    SoftwareArchitectAgent softwareArchitectAgent;

    @Inject
    SoftwareDeveloperAgent softwareDeveloperAgent;

    @Tool("Call the ResearcherAgent to conduct research on requirements, technologies, and best practices")
    public String callResearcher(@P("The user needs to investigate") String needs) {
        try {
            return researcherAgent.invoke(needs);
        } catch (Exception e) {
            return "Error calling ResearcherAgent: " + e.getMessage();
        }
    }

    @Tool("Call the SiftwareArchitectAgent to design system architecture based on research findings")
    public String callSoftwareArchitect(@P("The research findings and requirements to design architecture for") String researchOutput) {
        try {
            return softwareArchitectAgent.invoke(researchOutput);
        } catch (Exception e) {
            return "Error calling ArchitectAgent: " + e.getMessage();
        }
    }

    @Tool("Call the CoderAgent to implement the designed solution by writing actual code files")
    public String callSoftwareDeveloper(
            @P("The architecture specification") String architectureSpec,
            @P("The Implementation requirements") String requirements) {
        try {
            return softwareDeveloperAgent.invoke(architectureSpec, requirements);
        } catch (Exception e) {
            return "Error calling CoderAgent: " + e.getMessage();
        }
    }
}
