import {Component, EventEmitter, Inject, Input, OnInit, Output} from '@angular/core';
import {ChatService} from "../../services/chat.service";
import {WebSocketService} from "../../services/web-socket.service";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Message} from "../../models/message.model";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
  @Input() recipient!: string;
  @Input() emitter!: string;
  userId: number;
  userInput: string = '';
  isTyping: boolean = false;
  @Output() closeChat = new EventEmitter<string>();
  message: string = '';
  messages: Message[] = [];
  chatMessages: string[] = [];

  constructor(private chatService: ChatService, private webSocketService: WebSocketService,
              @Inject(MAT_DIALOG_DATA) public data: any) {
    this.userId = data.userId;
  }

  sendMessage() {
    // if (this.message.trim() !== '') {
    //   this.chatService.sendMessage(`/app/chat/${this.userId}`, this.message);
    //   this.message = '';
    // }
  }

  ngOnInit() {
    // Subscribe to chat messages for this specific user
    // this.chatService.subscribe(`/topic/chat/${this.userId}`, (message: string) => {
    //   this.chatMessages.push(message);
    // });
  }

  exit() {
    // this.closeChat.emit(this.recipient);
  }

  markAsRead() {
  }

  typing() {

  }
}
