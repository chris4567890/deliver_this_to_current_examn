package org.example;

import org.config.ApplicationConfig;
import org.config.Routes;

public class Main {
    public static void main(String[] args) {
        //System.out.println("Hello world!");
        boolean isTesting = false;
       /* ApplicationConfig instance = ApplicationConfig
                .getInstance()
                .startServer(7070)
                .checkSecurityRoles()
                .setRoute(Routes.getRoutes(false))
                .configureCors();
    }*/
        ApplicationConfig applicationConfig = ApplicationConfig.getInstance();
        applicationConfig.initiateServer()
                .startServer(7070)
                .checkSecurityRoles()
                .configureCors()
                .setRoute(Routes.getRoutes(isTesting))
                ;

    }
}