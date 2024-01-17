package com.ems.emschat.controller;

import com.ems.emschat.domain.dto.MessageDTO;
import com.ems.emschat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{emitter}/{recipient}")
    public ResponseEntity<List<MessageDTO>> getMessages(@PathVariable String emitter, @PathVariable String recipient) {
        List<MessageDTO> messageList = chatService.getMessages(emitter, recipient);
        return ResponseEntity.ok(messageList);
    }

    @PostMapping
    public ResponseEntity<?> messageReadNotification(@RequestBody List<Integer> messageIds) {
        chatService.messageReadNotification(messageIds);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/typing/{emitter}/{recipient}")
    public ResponseEntity<MessageDTO> sendTypingNotification(@PathVariable String emitter, @PathVariable String recipient, @RequestBody String text) {
        chatService.typingNotification(emitter, recipient, text);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{emitter}/{recipient}")
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable String emitter, @PathVariable String recipient, @RequestBody String text) {

        return ResponseEntity.status(HttpStatus.OK).body(chatService.sendMessage(emitter, recipient, text));
    }

}
