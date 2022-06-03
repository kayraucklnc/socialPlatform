package com.segmentationfault.huceng.usecases.chat.service;

import com.segmentationfault.huceng.entity.ChatMessage;
import com.segmentationfault.huceng.entity.MessageStatus;
import com.segmentationfault.huceng.entity.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Stream;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService service;


    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public Long countNewMessages(Long senderId, Long recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(senderId, recipientId, MessageStatus.RECEIVED);
    }

    @Override
    public Collection<ChatMessage> getAllChatMessages(Long senderId, Long recipientId) {
        var chatRoomId = service.getChatRoomId(senderId, recipientId);
        var chatRoomIdReverse = service.getChatRoomId(recipientId, senderId);

        var messages = chatRoomId
                .map(repository::findByChatRoomId).orElse(new ArrayList<>());

        var messagesReverse = chatRoomIdReverse
                .map(repository::findByChatRoomId).orElse(new ArrayList<>());

        if (messages.size() > 0) updateMessageStatus(senderId, recipientId, MessageStatus.DELIVERED);

        return Stream.concat(messages.stream(), messagesReverse.stream())
                .sorted(Comparator.comparingLong(message -> message.getTimestamp().getTime()))
                .toList();
    }

    @Override
    public ChatMessage getMessage(Long id) {
        return repository.findById(id)
                .map(message -> {
                    message.setStatus(MessageStatus.DELIVERED);
                    return repository.save(message);
                })
                .orElseThrow();
    }

    @Override
    public void updateMessageStatus(Long senderId, Long recipientId, MessageStatus status) {
        var chatRoomId = service.getChatRoomId(senderId, recipientId);
        var messages = chatRoomId
                .map(repository::findByChatRoomId).orElse(new ArrayList<>());

        //Will this update in the backend layer? Not sure.
        messages.forEach((message) -> {
            if (Objects.equals(message.getSenderId(), senderId) &&
                    Objects.equals(message.getRecipientId(), recipientId))
                message.setStatus(status);
        });
    }
}
