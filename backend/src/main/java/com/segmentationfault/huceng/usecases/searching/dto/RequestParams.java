package com.segmentationfault.huceng.usecases.searching.dto;

import lombok.Data;

@Data
public class RequestParams {
    private String searchTerm;
    private int page;
    private int size;
}
