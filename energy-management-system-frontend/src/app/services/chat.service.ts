import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Message} from "../models/message.model";
import {MessageDTO} from "../models/message.dto";
import {BehaviorSubject} from "rxjs";
import {LoggedInUserModel} from "../models/logged-in-user.model";

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private baseUrl = 'http://localhost:8084/chat';

  loggedUser = new BehaviorSubject<LoggedInUserModel>(null!);

  constructor(private httpClient: HttpClient) {
  }

  getMessages(emitter: string, recipient: string) {
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.get<MessageDTO[]>(`${this.baseUrl}/${emitter}/${recipient}`, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    });
  }

  markAsRead(messages: string[]) {
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.post(`${this.baseUrl}`, messages, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    });
  }

  sendIsTypingOrNoNotification(emitter: string, recipient: string, message: string) {
    if (message === '' || message === null) {
      message = 'no';
    }

    let token = sessionStorage.getItem("access_token");

    return this.httpClient.post(`${this.baseUrl}/typing/${emitter}/${recipient}`, message, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    });
  }

  sendMessage(message: string, recipient: string, emitter: string) {
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.post<Message>(`${this.baseUrl}/${emitter}/${recipient}`, message, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      }
    });
  }

}
