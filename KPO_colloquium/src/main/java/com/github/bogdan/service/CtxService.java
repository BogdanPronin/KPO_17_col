package com.github.bogdan.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.bogdan.exception.WebException;
import com.github.bogdan.model.JsonMessage;
import io.javalin.http.Context;

import java.util.ArrayList;

public class CtxService {
    public static void checkBodyRequestIsEmpty(Context ctx) throws JsonProcessingException {
        if(ctx.body().isEmpty())
            throw new WebException("Body request is empty",400);
    }



    public static void youAreNotAdmin(Context ctx) throws JsonProcessingException {
        throw new WebException("You aren't admin",401);
    }

    public static void created(Context ctx) throws JsonProcessingException {
        stringCtxObjectValue("Created",201,ctx);
    }

    public static void deleted(Context ctx) throws JsonProcessingException {
        stringCtxObjectValue("Deleted",200,ctx);
    }

    public static void updated(Context ctx) throws JsonProcessingException {
        stringCtxObjectValue("Updated",200,ctx);
    }

    public static void stringCtxObjectValue(String message, int status,Context ctx) throws JsonProcessingException {
        JsonMessage jsonMessage= new JsonMessage(message,status);
        ObjectMapper objectMapper = new ObjectMapper();
        ctx.result(objectMapper.writeValueAsString(jsonMessage));
        ctx.status(status);
    }

    public static void checkDoesBasicAuthEmpty(Context ctx) throws JsonProcessingException {
        if (!ctx.basicAuthCredentialsExist())
            throw new WebException("Basic auth is empty",400);
    }

    public static void checkDoesRequestBodyEmpty(Context ctx) throws JsonProcessingException {
        if(ctx.body().isEmpty())
            throw new WebException("Request body is empty",401);
    }
    public static void checkDoesQueryParamEmpty(Context ctx, String key) throws JsonProcessingException {
        if(ctx.queryParam(key)==null)
            throw new WebException("Parameter \""+key+ "\" is empty",400);
    }
    public static boolean doesQueryParamEmpty(Context ctx, String key){
        return ctx.queryParam(key) == null;
    }
    public static boolean doesQueryParamsEmpty(Context ctx, ArrayList<String> s){
        for(String a:s){
            if(ctx.queryParam(a)!=null)
                return false;
        }
        return true;
    }
}
