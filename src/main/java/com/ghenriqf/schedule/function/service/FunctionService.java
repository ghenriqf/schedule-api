package com.ghenriqf.schedule.function.service;

import com.ghenriqf.schedule.function.dto.FunctionResponse;
import com.ghenriqf.schedule.function.entity.Function;
import com.ghenriqf.schedule.function.repository.FunctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionService {

    private final FunctionRepository functionRepository;

    public List<FunctionResponse> findAll () {
        return functionRepository.findAll()
                .stream()
                .map(function -> FunctionResponse
                        .builder()
                        .id(function.getId())
                        .name(function.getName())
                        .build())
                .toList();
    }
}
