package com.ems.emschat.repository;

import com.ems.emschat.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    List<Message> findBySenderAndReceiverOrReceiverAndSender(String sender, String receiver, String receiver2, String sender2);
}
