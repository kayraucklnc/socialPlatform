package com.segmentationfault.huceng.usecases.requestUserInfo.dto;

import lombok.Data;

@Data
public class PagingRequest {
    private int page;
    private int size;
}
