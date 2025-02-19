package com.indosat.ipification.controller;

import com.indosat.ipification.dto.IRequest;
import com.indosat.ipification.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/indosat/checknohp")
@RequiredArgsConstructor
public class UserInfoController {

    private final UserInfoService userInfoService;

    @PostMapping("token")
    public ResponseEntity<IRequest> getUserInfo(@RequestBody IRequest request) {
        log.info("Received request for user info: {}", request);
        IRequest response = userInfoService.getUserInfo(request);

        if (response.getVerified() == null) {
            log.warn("User info not found for: {}", request.getNohp());
            return ResponseEntity.notFound().build();
        }

        log.info("Returning response: {}", response);
        return ResponseEntity.ok(response);
    }
}
