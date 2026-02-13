package com.ghenriqf.schedule.function.controller;

import com.ghenriqf.schedule.function.dto.FunctionResponse;
import com.ghenriqf.schedule.function.service.FunctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/functions")
@RequiredArgsConstructor
public class FunctionController {

    private final FunctionService functionService;

    @GetMapping
    public List<FunctionResponse> findAll () {
        return functionService.findAll();
    }
}
