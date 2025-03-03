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

    public LogEntity saveRequestLog(String noHp, String message, String service) {
        LogEntity log = new LogEntity();
        log.setUid(UUID.randomUUID());
        log.setNoHp(noHp);
        log.setService(service);
        log.setRequest(message);
        log.setRequestDate(new Date());

        lr.save(log);
        return log;
    }

    public void updateResponseLog(LogEntity log, String message, String rc, Boolean result) {
        log.setResponse(message);
        log.setResponseDate(new Date());
        log.setRc(rc);
        log.setResult(result);

        lr.save(log);
    }

    public void errorResponse(String noHp, String service, String message) {
        LogEntity log = new LogEntity();
        log.setUid(UUID.randomUUID());
        log.setNoHp(noHp);
        log.setService(service);
        log.setRequest("");
        log.setRequestDate(new Date());
        log.setResponse(message);
        log.setResponseDate(new Date());
        log.setRc("99");
        log.setResult(Boolean.FALSE);

        lr.save(log);
    }
}
