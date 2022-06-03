package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.ChatMessage;
import com.segmentationfault.huceng.entity.MessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    Long countBySenderIdAndRecipientIdAndStatus(Long senderId, Long recipientId, MessageStatus status);

    List<ChatMessage> findByChatRoomId(Long chatId);

}
