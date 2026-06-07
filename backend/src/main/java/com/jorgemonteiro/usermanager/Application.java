package com.jorgemonteiro.usermanager;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Entry point for the User Profile Manager Quarkus application.
 */
@QuarkusMain
public class Application {

    /**
     * Starts the Quarkus runtime.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Quarkus.run(args);
    }
}
