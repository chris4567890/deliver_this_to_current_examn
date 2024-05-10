package org.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nimbusds.jose.shaded.json.JSONObject;
import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.HttpStatus;
import org.controllers.SecurityController;
import org.dtos.SellerDTO;
import org.hibernate.boot.jaxb.internal.MappingBinder;
import org.model.Role;

//import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.header;

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
            config.accessManager((handler, ctx, permittedRoles)->{

                if(ctx.method().toString().equals("OPTIONS")){
                    ctx.status(200);
                    return;
                }

                if(permittedRoles.contains(Role.anyone)){
                    ctx.status(200);
                    handler.handle(ctx);
                    return;
                }
                JsonObject returnObject = new JsonObject();
                String header = ctx.header("Authorization");
                if(header == null){
                    returnObject.addProperty("msg", "Authorization header missing");
                    ctx.status(HttpStatus.FORBIDDEN).json(returnObject);
                    return;
                }
                String token = header.split(" ")[1];
                if (token == null) {
                    returnObject.addProperty("msg", "Authorization header missing");
                    ctx.status(HttpStatus.FORBIDDEN).json(returnObject);
                    return;
                }
                SecurityController sc = SecurityController.getInstance();
                SellerDTO verifiedTokenUser = sc.verifyToken(token);
                if (verifiedTokenUser == null) {
                    returnObject.addProperty("msg", "Invalid User or Token");
                    ctx.status(HttpStatus.FORBIDDEN).json(returnObject);
                    return;
                }
                if(!ctx.path().isEmpty() && verifiedTokenUser.getRoles().stream().noneMatch(x -> x.equals(ctx.path()))){
                    returnObject.addProperty("msg", "You don't have access to this part of the website");
                    ctx.status(HttpStatus.FORBIDDEN).json(returnObject);
                    return;
                }
                System.out.println("USER IN AUTHENTICATE: " + verifiedTokenUser);
                ctx.attribute("user", verifiedTokenUser);

            });
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



