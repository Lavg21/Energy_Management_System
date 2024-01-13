import {EventEmitter, Injectable, Input, Output} from '@angular/core';
import {Message} from "../models/message.model";
import {WebSocketService} from "./web-socket.service";

@Injectable({
  providedIn: 'root'
})
export class ChatService {


  sendMessage(s: string, message: string) {
    return null;
  }
}
