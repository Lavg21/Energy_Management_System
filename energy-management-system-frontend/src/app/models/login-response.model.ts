export class LoginResponseModel {

  token!: string;

  constructor(token: string) {
    this.token = token;
  }
}
