export class DeviceModel {

  id!: number;
  description!: string;
  address!: string;
  consumption!: string;

  constructor(id: number, description: string, address: string, consumption: string) {
    this.id = id;
    this.description = description;
    this.address = address;
    this.consumption = consumption;
  }
}
