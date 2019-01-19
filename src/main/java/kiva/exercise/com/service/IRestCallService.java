package kiva.exercise.com.service;

import java.util.concurrent.CompletableFuture;

public interface IRestCallService {
    /**
     *
     * This function is used to make rest api calls.
     *
     * @param url This is the URI for the resource. The base path is taken form the config.
     * @param cls The response type Class
     * @return The response body.
     *
     */
    CompletableFuture<?> makeGetCall(String url,Class<?> cls);
}
