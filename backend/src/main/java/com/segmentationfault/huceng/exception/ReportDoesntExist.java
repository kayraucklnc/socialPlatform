package com.segmentationfault.huceng.exception;

public class ReportDoesntExist extends RuntimeException {

    public ReportDoesntExist(Long reportId) {
        super("Report with id: " + reportId + " does not exist.");
    }
}
