export class Message {
  id!: string
  text!: string;
  sender!: 'sent' | 'received';
  read!: boolean;
}
