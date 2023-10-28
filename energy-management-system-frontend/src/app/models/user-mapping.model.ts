export class UserMappingModel{
  name!: string;
  email!: string;
  checked!: boolean;


  constructor(name: string, email: string, checked: boolean) {
    this.name = name;
    this.email = email;
    this.checked = checked;
  }
}
