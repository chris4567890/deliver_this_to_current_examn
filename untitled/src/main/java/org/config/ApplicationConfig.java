package org.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
//source for this code is one of my previous group projects
    public class ApplicationConfig {

        //private static ObjectMapper jsonMapper = new ObjectMapper();
        private static ObjectMapper jsonMapper = new ObjectMapper();
        private static org.config.ApplicationConfig appConfig;

        private static Javalin app;

        private ApplicationConfig(){}

        public static org.config.ApplicationConfig getInstance(){
            if(appConfig == null){
                appConfig = new org.config.ApplicationConfig();
            }
            return appConfig;
        }

        public org.config.ApplicationConfig initiateServer(){
            System.out.println("Working Directory = "+ System.getProperty("user.dir"));
            String separator = System.getProperty("file.separator");
            app = Javalin.create(config -> {
                config.http.defaultContentType="application/json";
                config.routing.contextPath="/api";
                config.plugins.enableDevLogging();
            });
            return appConfig;
        };


        public org.config.ApplicationConfig setRoutes(EndpointGroup routes){
            app.routes(routes);
            return appConfig;
        }

        public org.config.ApplicationConfig startServer(int port){
            app.start(port);
            return appConfig;
        };

        public org.config.ApplicationConfig setExceptionHandling(){

            app.exception(IllegalStateException.class, (e, ctx) -> {
                ObjectNode json = jsonMapper.createObjectNode();
                json.put("errorMessage", e.getMessage());
                e.printStackTrace();
                ctx.status(500).json(json);
            });
            app.exception(Exception.class, (e, ctx) -> {
                ObjectNode json = jsonMapper.createObjectNode();
                json.put("errorMessage",e.getMessage());
                e.printStackTrace();
                ctx.status(500).json(json);
            });
            app.error(404, ctx -> {
                ObjectNode json = jsonMapper.createObjectNode();
                json.put("errorMessage", "Not found");
                ctx.status(404).json(json);
            });
            return appConfig;
        }

        public org.config.ApplicationConfig closeServer(){
            app.close();
            return appConfig;
        }

    }



