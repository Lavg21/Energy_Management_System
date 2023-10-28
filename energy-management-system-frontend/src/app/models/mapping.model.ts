export class MappingModel {

  userID!: number;
  deviceID!: number;
  name!: string
  email!: string;
  deviceDescription!: string;
  deviceAddress!: string;

  constructor(userID: number, deviceID: number) {
    this.userID = userID;
    this.deviceID = deviceID;
  }
}
