package com.segmentationfault.huceng.util;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ErrorWrapper {
    private Integer code;
    private String message;

    public ErrorWrapper(int i, String message) {
        this.code = i;
        this.message = message;
    }
}
