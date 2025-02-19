package com.indosat.ipification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.indosat.ipification.entity.CounterEntity;
import com.indosat.ipification.repository.CounterRepository;
import com.indosat.ipification.repository.ParameterRepository;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final CounterRepository counterRepository;
    private final ParameterRepository parameterRepository;

    public boolean isLimitExceeded(String noHp) {
        int limit = Integer.parseInt(parameterRepository.findById("ParameterNohp").get().getValue());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        CounterEntity counter = counterRepository.findById(noHp).orElseGet(() -> {
            CounterEntity newCounter = new CounterEntity();
            newCounter.setPhone(noHp);
            newCounter.setDate(new Date());
            newCounter.setCount(0);
            return counterRepository.save(newCounter);
        });

        int count = counter.getCount();
        if (sdf.format(counter.getDate()).equals(sdf.format(new Date()))) {
            count++;
        } else {
            count = 1;
        }

        counter.setCount(count);
        counter.setDate(new Date());
        counterRepository.save(counter);

        return count > limit;
    }
}
