package com.ghenriqf.schedule.scale.service;

import com.ghenriqf.schedule.scale.repository.ScaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ScaleService {

    private final ScaleRepository scaleRepository;


    public Long countByMinistryIdAndDateAfter (Long id, LocalDateTime date) {
        return scaleRepository.countByMinistryIdAndDateAfter(id, date);
    }
}

