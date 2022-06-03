package com.segmentationfault.huceng.usecases.report.dto;

import lombok.Data;

@Data
public class BanRequest {
    private final Boolean indefinite;
    private final Long duration;
}
