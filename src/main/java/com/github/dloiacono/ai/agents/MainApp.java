package com.github.dloiacono.ai.agents;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class MainApp {

    public static void main(String ... args) {
        Quarkus.run(AgentApplication.class, args);
    }
}