package com.ems.emschat.service;

import com.ems.emschat.domain.dto.MessageDTO;
import com.ems.emschat.domain.entity.Message;
import com.ems.emschat.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public List<MessageDTO> getMessages(String sender, String receiver) {
        return convertToDTOList(
                messageRepository.findBySenderAndReceiverOrReceiverAndSender(sender, receiver, sender, receiver));
    }

    public void messageReadNotification(List<Integer> messagesIds) {
        List<Message> messages = messageRepository.findAllById(messagesIds);

        messages.forEach(message -> message.setSeen(true));

        messageRepository.saveAll(messages);

        messages.forEach(message -> simpMessagingTemplate.convertAndSend(
                "/topic/socket/messages/" + message.getSender() + "/" + message.getReceiver() + "/seen", message));

    }

    public void typingNotification(String emitter, String recipient, String messageText) {
        simpMessagingTemplate.convertAndSend("/topic/socket/messages/" + recipient + "/" + emitter + "/typing", messageText);
    }

    @Transactional
    public MessageDTO sendMessage(String emitter, String recipient, String messageText) {

            MessageDTO messageDTO = MessageDTO.builder()
                    .sender(emitter)
                    .receiver(recipient)
                    .messageText(messageText)
                    .build();

            Message message = messageRepository.save(convertToEntity(messageDTO));

            simpMessagingTemplate.convertAndSend("/topic/socket/messages/" + recipient + "/" + emitter, message);
            simpMessagingTemplate.convertAndSend("/topic/socket/messages/" + recipient + "/" + emitter + "/typing", "no");

            return convertToDTO(message);
    }

    private Message convertToEntity(MessageDTO messageDTO) {

        return Message.builder()
                .sender(messageDTO.getSender())
                .receiver(messageDTO.getReceiver())
                .messageText(messageDTO.getMessageText())
                .seen(false)
                .build();
    }

    private MessageDTO convertToDTO(Message message) {

        return MessageDTO.builder()
                .id(message.getId())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .messageText(message.getMessageText())
                .seen(message.getSeen())
                .build();
    }

    private List<MessageDTO> convertToDTOList(List<Message> messages) {
        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
