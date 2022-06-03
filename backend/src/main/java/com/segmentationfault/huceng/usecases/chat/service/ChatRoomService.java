package com.segmentationfault.huceng.usecases.chat.service;

import java.util.Optional;

public interface ChatRoomService {

    Optional<Long> getChatRoomId(Long senderId, Long recipientId);

}
