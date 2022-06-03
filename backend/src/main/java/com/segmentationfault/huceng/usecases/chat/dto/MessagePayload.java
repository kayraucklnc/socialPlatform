package com.segmentationfault.huceng.usecases.chat.dto;

import lombok.Data;

@Data
public class MessagePayload {
    private Long senderId;
    private Long recipientId;
    private String senderName;
    private String recipientName;
    private String content;
}
