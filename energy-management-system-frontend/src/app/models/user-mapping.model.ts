export class UserMappingModel{
  id!: number;
  name!: string;
  email!: string;
  checked!: boolean;


  constructor(id: number, name: string, email: string, checked: boolean) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.checked = checked;
  }
}
