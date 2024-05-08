package org.example;

import org.config.ApplicationConfig;
import org.config.Routes;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        boolean isTesting = false;
        ApplicationConfig app = ApplicationConfig.getInstance().initiateServer().setExceptionHandling().startServer(7070).setRoutes(Routes.getRoutes(isTesting));
    }
}