package com.indosat.ipification.utils;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.indosat.ipification.entity.LogEntity;
import com.indosat.ipification.repository.LogRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LogUtils {
    private final LogRepository lr;

    public void saveLog(String noHp, String message, String service, boolean isRequest, String rc, Boolean result) {
        LogEntity log = new LogEntity();
        log.setUid(UUID.randomUUID());
        log.setNoHp(noHp);
        log.setService(service);

        if (isRequest) {
            log.setRequest(message);
            log.setRequestDate(new Date());
        } else {
            log.setResponse(message);
            log.setResponseDate(new Date());
            log.setRc(rc);
            log.setResult(result);
        }

        lr.save(log);
    }
}