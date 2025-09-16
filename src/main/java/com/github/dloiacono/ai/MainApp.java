package com.github.dloiacono.ai;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class MainApp {

    public static void main(String ... args) {
        Quarkus.run(AgentApplication.class, args);
    }
}