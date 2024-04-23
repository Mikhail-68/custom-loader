package ru.mts.customloader.dto;

import lombok.Data;

@Data
public class LoadDto {
    private String host;
    private String endpoint;
    private int rps;
    private int periodInSeconds;
}
