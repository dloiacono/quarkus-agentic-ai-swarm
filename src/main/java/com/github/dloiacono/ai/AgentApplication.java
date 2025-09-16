package com.github.dloiacono.ai;

import com.github.dloiacono.ai.agents.SupervisorAgent;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

@ActivateRequestContext
public class AgentApplication implements QuarkusApplication {

    @Inject
    SupervisorAgent supervisorAgent;

    @Override
    public int run(String... args) throws Exception {


        System.out.println(supervisorAgent.invoke(
                        "Create a REST API using Quarkus that perform simple calculations between two numbers"));
        Quarkus.asyncExit();
        return 0;
    }
}
