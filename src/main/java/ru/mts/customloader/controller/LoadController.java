package ru.mts.customloader.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mts.customloader.dto.LoadDto;
import ru.mts.customloader.service.LoadService;

@RestController
@RequestMapping("/api/v1/load")
@RequiredArgsConstructor
public class LoadController {
    private final LoadService loadService;

    @GetMapping
    public void load(@RequestBody LoadDto loadDto) {
        loadService.load(loadDto);
    }
}
