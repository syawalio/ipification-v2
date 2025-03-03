package com.indosat.ipification.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indosat.ipification.dto.IRequest;
import com.indosat.ipification.dto.IResponse;
import com.indosat.ipification.dto.UserInfoResponse;
import com.indosat.ipification.entity.LogEntity;
import com.indosat.ipification.utils.LogUtils;

@ExtendWith(MockitoExtension.class)
public class IpificationServiceTest {

    @Mock
    private LogUtils logUtils;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private IpificationService ipificationService;

    @Value("${ipification.token.url}")
    private String TOKEN_URL;

    @Value("${ipification.userinfo.url}")
    private String USERINFO_URL;

    @BeforeEach
    void setUp() {
        ipificationService = new IpificationService(logUtils, restTemplate);
    }

    @Test
    void testSendHttpPostToken() {
        IRequest request = new IRequest();
        request.setCode("testCode");
        request.setState("testState");
        request.setNohp("1234567890");

        IResponse expectedResponse = new IResponse();
        expectedResponse.setAccess_token("testAccessToken");

        LogEntity logEntity = new LogEntity();
        when(logUtils.saveRequestLog(any(), any(), any())).thenReturn(logEntity);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"access_token\":\"testAccessToken\"}", HttpStatus.OK);
        when(restTemplate.exchange(eq(TOKEN_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        IResponse actualResponse = ipificationService.sendHttpPostToken(request);

        verify(logUtils).saveRequestLog(any(), any(), any());
        verify(logUtils).updateResponseLog(any(), any(), any(), any());
        assertEquals(expectedResponse.getAccess_token(), actualResponse.getAccess_token());
    }

    @Test
    void testSendHttpGet() {
        IRequest request = new IRequest();
        request.setNohp("1234567890");

        IResponse tokenResponse = new IResponse();
        tokenResponse.setAccess_token("testAccessToken");

        UserInfoResponse expectedResponse = new UserInfoResponse();
        expectedResponse.setPhone_number_verified("true");

        LogEntity logEntity = new LogEntity();
        when(logUtils.saveRequestLog(any(), any(), any())).thenReturn(logEntity);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("{\"phone_number_verified\":true}", HttpStatus.OK);
        when(restTemplate.exchange(eq(USERINFO_URL), eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(responseEntity);

        UserInfoResponse actualResponse = ipificationService.sendHttpGet(request, tokenResponse);

        verify(logUtils).saveRequestLog(any(), any(), any());
        verify(logUtils).updateResponseLog(any(), any(), any(), any());
        assertEquals(expectedResponse.getPhone_number_verified(), actualResponse.getPhone_number_verified());
    }
}
