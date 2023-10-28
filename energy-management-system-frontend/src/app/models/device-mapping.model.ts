export class DeviceMappingModel{
  id!: number;
  description!: string;
  address!: string;
  checked!: boolean;


  constructor(id: number, description: string, address: string, checked: boolean) {
    this.id = id;
    this.description = description;
    this.address = address;
    this.checked = checked;
  }
}
