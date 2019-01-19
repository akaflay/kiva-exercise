package code.better.com.service;

import java.util.concurrent.CompletableFuture;

public interface IRestCall {
    CompletableFuture<String> makeGetCall(String url);
}
