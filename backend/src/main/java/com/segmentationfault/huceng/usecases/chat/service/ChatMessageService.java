package com.segmentationfault.huceng.usecases.chat.service;

import com.segmentationfault.huceng.entity.ChatMessage;
import com.segmentationfault.huceng.entity.MessageStatus;

import java.util.Collection;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);

    Long countNewMessages(Long senderId, Long recipientId);

    Collection<ChatMessage> getAllChatMessages(Long senderId, Long recipientId);

    ChatMessage getMessage(Long id);

    void updateMessageStatus(Long senderId, Long recipientId, MessageStatus status);



}
