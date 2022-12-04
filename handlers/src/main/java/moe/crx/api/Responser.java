package moe.crx.api;

import jakarta.ws.rs.core.Response;

public final class Responser {

    public static Response dropOk(Object response) {
        return Response
                .ok(response)
                .build();
    }

    public static Response drop(Response.Status status, String errorMessage) {
        return Response
                .status(status)
                .entity(errorMessage)
                .build();
    }

    public static Response drop(String errorMessage) {
        return drop(Response.Status.BAD_REQUEST, errorMessage);
    }
}
