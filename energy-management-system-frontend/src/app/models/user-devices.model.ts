export class UserDevicesModel {

  id!: number;
  description!: string;
  address!: string;
  consumption!: string;
  userId!: number;

  constructor(id: number, description: string, address: string, consumption: string, userId: number) {
    this.id = id;
    this.description = description;
    this.address = address;
    this.consumption = consumption;
    this.userId = userId;
  }
}
