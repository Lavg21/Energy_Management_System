export class AddEditDeviceModel {

  description!: string;
  address!: string;
  consumption!: number;


  constructor(description: string, address: string, consumption: number) {
    this.description = description;
    this.address = address;
    this.consumption = consumption;
  }
}
