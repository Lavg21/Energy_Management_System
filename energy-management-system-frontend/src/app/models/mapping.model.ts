export class MappingModel {

  userId!: number;
  deviceId!: number;

  constructor(userId: number, deviceId: number) {
    this.userId = userId;
    this.deviceId = deviceId;
  }
}
