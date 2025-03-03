package com.indosat.ipification.service;

import java.net.InetSocketAddress;
import java.net.Proxy;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indosat.ipification.dto.IRequest;
import com.indosat.ipification.dto.IResponse;
import com.indosat.ipification.dto.UserInfoResponse;
import com.indosat.ipification.entity.LogEntity;
import com.indosat.ipification.utils.LogUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IpificationService {

    private final LogUtils logUtils;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TOKEN_URL = "https://api.stage.ipification.com/auth/realms/ipification/protocol/openid-connect/token";
    private static final String USERINFO_URL = "https://api.stage.ipification.com/auth/realms/ipification/protocol/openid-connect/userinfo";

    @Value("${client.id}")
    private String CLIENT_ID;

    @Value("${client.secret}")
    private String CLIENT_SECRET;

    @Value("${proxy.host}")
    private String PROXY_HOST;

    @Value("${proxy.port}")
    private int PROXY_PORT;

    public IpificationService() {
        this.logUtils = null;
        this.restTemplate = new RestTemplate();
    }
    
    public IpificationService(LogUtils logUtils) {
        this.logUtils = logUtils;
    
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 seconds
        factory.setReadTimeout(5000); // 5 seconds
    
        // Set proxy if available
        if (PROXY_HOST != null && !PROXY_HOST.isEmpty() && PROXY_PORT > 0) {
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
            factory.setProxy(proxy);
            log.info("Using proxy: {}:{}", PROXY_HOST, PROXY_PORT);
        }
    
        this.restTemplate = new RestTemplate(factory);
    }

    public IResponse sendHttpPostToken(IRequest req) {
        IResponse responseToken = new IResponse();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("redirect_uri", "http://10.41.224.100/ipvication");
        body.add("grant_type", "authorization_code");
        body.add("code", req.getCode());
        body.add("state", req.getState());
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        LogEntity logData = logUtils.saveRequestLog(req.getNohp(), new JSONObject(body).toString(), "token");
        
        try {
            log.info("Requesting token for noHp={} to URL={}", req.getNohp(), TOKEN_URL);
            log.info("check response {} : {}", requestEntity, TOKEN_URL);

            ResponseEntity<String> response = restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class);
            String responseStr = response.getBody();
            log.info("Token response received for noHp={} from URL={} : {}", req.getNohp(), TOKEN_URL, responseStr);

            if (responseStr != null && responseStr.contains("access_token")) {
                JSONObject jsonResponse = new JSONObject(responseStr);
                responseToken.setAccess_token(jsonResponse.optString("access_token", ""));
            } else {
                log.warn("Token response does not contain an access_token: {}", responseStr);
            }
            

            logUtils.updateResponseLog(logData, responseStr, response.getStatusCode().toString(), null);
        } catch (Exception e) {
            log.info("Error retrieving token for noHp={} from URL={} : {}", req.getNohp(), TOKEN_URL, e.getMessage());
            logUtils.updateResponseLog(logData, "Error retrieving token", "99", null);
        }

        return responseToken;
    }

    public UserInfoResponse sendHttpGet(IRequest req, IResponse respToken) {
        UserInfoResponse userInfo = new UserInfoResponse();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("access_token", respToken.getAccess_token());
    
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
        LogEntity logData = logUtils.saveRequestLog(req.getNohp(), respToken.getAccess_token(), "userinfo");
    
        try {
            log.info("Requesting user info for noHp={} via POST to URL={}", req.getNohp(), USERINFO_URL);
    
            ResponseEntity<String> response = restTemplate.exchange(USERINFO_URL, HttpMethod.POST, requestEntity, String.class);
            log.info("User info response received for noHp={} : {}", req.getNohp(), response.getBody());
    
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            boolean verified = jsonResponse.get("phone_number_verified").asBoolean();
            userInfo.setPhone_number_verified(String.valueOf(verified));
    
            logUtils.updateResponseLog(logData, response.getBody(), response.getStatusCode().toString(), verified);
        } catch (Exception e) {
            log.error("Error retrieving user info for noHp={} : {}", req.getNohp(), e.getMessage(), e);
            logUtils.updateResponseLog(logData, "Error retrieving user info", "99", null);
        }
    
        return userInfo;
    }
}
