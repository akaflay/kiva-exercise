package kiva.exercise.com.service.impl;

import kiva.exercise.com.service.IRestCallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class RestCallServiceImpl implements IRestCallService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${kiva.base.url}")
    private String KIVA_BASE_URL;
    @Value("${kiva.version}")
    private String KIVA_VERSION;

    @Override
    public CompletableFuture<?> makeGetCall(String url,Class<?> cls) {
        final ResponseEntity<?> responseEntity=restTemplate.getForEntity(
                String.format("%s%s",getBaseURL(),url),cls);
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            return CompletableFuture.completedFuture(responseEntity.getBody());
        }
        return CompletableFuture.completedFuture(null);
    }
    private String getBaseURL() {
        return String.format("%s%s", KIVA_BASE_URL, KIVA_VERSION);
    }
}
