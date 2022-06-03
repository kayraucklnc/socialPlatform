package com.segmentationfault.huceng.entity.repository;

import com.segmentationfault.huceng.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByOwnerIdAndRecipientId(Long ownerId, Long recipientId);

}
