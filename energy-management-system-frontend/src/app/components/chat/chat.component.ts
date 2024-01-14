import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {ChatService} from "../../services/chat.service";
import {WebSocketService} from "../../services/web-socket.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Message} from "../../models/message.model";
import {MessageDTO} from "../../models/message.dto";
import {UserModel} from "../../models/user.model";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  userInput: string = '';
  isTyping: boolean = false;
  @Output() closeChat = new EventEmitter<string>();
  message: string = '';
  messages: Message[] = [];
  chatMessages: string[] = [];
  emitter: UserModel;
  recipient: UserModel;

  constructor(private chatService: ChatService, private webSocketService: WebSocketService,
              @Inject(MAT_DIALOG_DATA) public data: any) {

    this.emitter = data.emitter;
    this.recipient = data.recipient;
  }

  sendMessage() {
    if (this.userInput.trim() === '') {
      return;
    }

    this.chatService.sendMessage(this.userInput, this.recipient.email, this.emitter.email).subscribe(data => {
      this.messages.push({text: this.userInput, sender: 'sent', read: false, id: data.id});
      this.userInput = '';
    });
  }

  ngOnInit() {
    this.chatService.getMessages(this.emitter.email, this.recipient.email).subscribe(data => {
      data.forEach((message: MessageDTO) => {
        console.log(message);
        console.log(this.emitter.email);
        if (message.sender === this.emitter.email) {
          this.messages.push({text: message.messageText, sender: 'sent', read: message.seen, id: message.id});
        } else {
          this.messages.push({text: message.messageText, sender: 'received', read: message.seen, id: message.id});
        }
      });

      this.webSocketService.connect("http://localhost:8084/socket");
      this.webSocketService.stompClient.connect({}, (frame: any) => {
        this.webSocketService.stompClient.subscribe("/topic/socket/messages/" + this.emitter.email + "/" + this.recipient.email, (message) => {
          const messageDto: MessageDTO = JSON.parse(message.body);
          this.messages.push({text: messageDto.messageText, sender: 'received', read: false, id: messageDto.id});
        });
      });
      this.webSocketService.secondStompClient.connect({}, (frame: any) => {
        this.webSocketService.secondStompClient.subscribe("/topic/socket/messages/" + this.emitter.email + "/" + this.recipient.email + "/seen", (message) => {
          this.messages.forEach(message => {
            if (message.sender === 'sent')
              message.read = true
          });
        });
      });

      this.webSocketService.thirdStompClient.connect({}, (frame: any) => {
        this.webSocketService.thirdStompClient.subscribe("/topic/socket/messages/" + this.emitter.email + "/" + this.recipient.email + "/typing", (message) => {
          if (!(message.body === 'no')) {
            this.isTyping = true;
          } else {
            this.isTyping = false;
          }
        });
      });

    });
  }

  exit() {
    this.closeChat.emit(this.recipient.email);
  }

  markAsRead() {
    if (this.messages.length === 0) {
      return;
    }
    const idUnreadMessages = this.messages.filter(message => !message.read && message.sender === 'received').map(message => message.id);
    this.chatService.markAsRead(idUnreadMessages).subscribe(data => {
      this.messages.filter(message => !message.read && message.sender === 'received').forEach(message => message.read = true);
    })
  }

  typing() {
    this.chatService.sendIsTypingOrNoNotification(this.emitter.email, this.recipient.email, this.userInput).subscribe(data => {
    });
  }
}
