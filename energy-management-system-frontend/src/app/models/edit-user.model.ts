export class EditUserModel {

  name!: string;
  email!: string;
  admin!: boolean;

  constructor(name: string, email: string, admin: boolean) {
    this.name = name;
    this.email = email;
    this.admin = admin;
  }
}
