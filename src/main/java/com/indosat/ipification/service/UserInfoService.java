package com.indosat.ipification.service;

import org.springframework.stereotype.Service;
import com.indosat.ipification.dto.IRequest;
import com.indosat.ipification.dto.IResponse;
import com.indosat.ipification.dto.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final IpificationService ipificationService;
    private final CounterService counterService;

    public IRequest getUserInfo(IRequest request) {
        log.info("Processing getUserInfo for {}", request.getNohp());

        if (counterService.isLimitExceeded(request.getNohp())) {
            log.warn("Request limit exceeded for {}", request.getNohp());
            request.setVerified(Boolean.FALSE);
            request.setMessage("Request limit exceeded. You can only verify up to 3 times per day.");
            return request;
        }

        IResponse resGetToken = ipificationService.sendHttpPostToken(request);
        if (resGetToken == null || resGetToken.getAccess_token() == null) {
            log.warn("Failed to retrieve access token.");
            request.setVerified(Boolean.FALSE);
            request.setMessage("Failed to retrieve access token.");
            return request;
        }

        UserInfoResponse respUserInfo = ipificationService.sendHttpGet(request, resGetToken);
        if (respUserInfo == null || respUserInfo.getPhone_number_verified() == null) {
            log.warn("User info response is invalid.");
            request.setVerified(Boolean.FALSE);
            request.setMessage("Invalid user info response.");
            return request;
        }

        if ("true".equalsIgnoreCase(respUserInfo.getPhone_number_verified())) {
            request.setVerified(Boolean.TRUE);
            request.setMessage("User verification successful.");
        } else {
            request.setVerified(Boolean.FALSE);
            request.setMessage("User verification failed.");
        }

        log.info("Final verification status: {}", request.getVerified());
        return request;
    }
}
