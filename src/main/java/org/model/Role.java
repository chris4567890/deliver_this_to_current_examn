package org.model;

import io.javalin.security.RouteRole;

public enum Role implements RouteRole {

    seller,
    admin,
    anyone


}
