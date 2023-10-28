export class DeviceMappingModel{
  description!: string;
  address!: string;
  checked!: boolean;


  constructor(description: string, address: string, checked: boolean) {
    this.description = description;
    this.address = address;
    this.checked = checked;
  }
}
