package com.segmentationfault.huceng.usecases.chat.controller;

import com.segmentationfault.huceng.entity.ChatMessage;
import com.segmentationfault.huceng.entity.ChatNotification;
import com.segmentationfault.huceng.usecases.chat.dto.MessagePayload;
import com.segmentationfault.huceng.usecases.chat.service.ChatMessageService;
import com.segmentationfault.huceng.usecases.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload MessagePayload message) {
        ChatMessage chatMessage = ChatMessage.builder()
                .senderId(message.getSenderId())
                .senderName(message.getSenderName())
                .recipientName(message.getRecipientName())
                .recipientId(message.getRecipientId())
                .content(message.getContent())
                .timestamp(new Date())
                .build();

        var chatRoomId = chatRoomService.getChatRoomId(message.getSenderId(), message.getRecipientId())
                .orElseThrow();
        chatMessage.setChatRoomId(chatRoomId);

        var saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(message.getRecipientId()),
                "/queue/messages",
                chatMessage
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable Long senderId,
            @PathVariable Long recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<?> findChatMessages(
            @PathVariable Long senderId,
            @PathVariable Long recipientId
    ) {
        return ResponseEntity.ok(chatMessageService.getAllChatMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{id}")
    public ResponseEntity<?> findMessage(@PathVariable Long id) {
        return ResponseEntity.ok(chatMessageService.getMessage(id));
    }
}
