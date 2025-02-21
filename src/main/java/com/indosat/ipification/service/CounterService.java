package com.indosat.ipification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.indosat.ipification.entity.CounterEntity;
import com.indosat.ipification.repository.CounterRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CounterService {

    private final CounterRepository counterRepository;

    public boolean isLimitExceeded(String noHp) {
        int limit = 3;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();

        Optional<CounterEntity> counterOpt = counterRepository.findById(noHp);
        CounterEntity counter = counterOpt.orElseGet(() -> {
            CounterEntity newCounter = new CounterEntity();
            newCounter.setPhone(noHp);
            newCounter.setDate(today);
            newCounter.setCount(0);
            return newCounter;
        });

        if (sdf.format(counter.getDate()).equals(sdf.format(today))) {
            if (counter.getCount() >= limit) {
                return true;
            }
            counter.setCount(counter.getCount() + 1);
        } else {
            counter.setCount(1);
            counter.setDate(today);
        }

        counterRepository.save(counter);
        return false;
    }
}
