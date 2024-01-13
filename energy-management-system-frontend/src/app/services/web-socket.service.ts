import { Injectable } from '@angular/core';
import * as SockJS from "sockjs-client";
import {CompatClient, Stomp} from "@stomp/stompjs";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  websocket!: WebSocket;
  stompClient!: CompatClient;
  secondWebsocket!: WebSocket;
  secondStompClient!: CompatClient;
  thirdWebsocket!: WebSocket;
  thirdStompClient!: CompatClient;
  constructor() { }


  connect(url:string) {

    this.websocket = new SockJS(url);
    this.stompClient = Stomp.over(this.websocket);
    this.secondWebsocket = new SockJS(url);
    this.secondStompClient = Stomp.over(this.secondWebsocket);
    this.thirdWebsocket = new SockJS(url);
    this.thirdStompClient = Stomp.over(this.thirdWebsocket);

  }
}
