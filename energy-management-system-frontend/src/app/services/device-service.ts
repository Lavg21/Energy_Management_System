import {Injectable} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {AddEditDeviceModel} from "../models/add-edit-device.model";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})


export class DeviceService {

  devicesUrl: string = "http://localhost:8081/device";

  constructor(private httpClient: HttpClient, private router: Router) {
  }

  getDeviceById(id: number) {
    let url: string = this.devicesUrl + "/" + id;

    return this.httpClient.get<HttpResponse<any>>(url, {
      headers: {
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  getAllDevices() {
    let url: string = this.devicesUrl;

    return this.httpClient.get<HttpResponse<any>>(url, {
      headers: {
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  addDevice(device: AddEditDeviceModel) {
    let url: string = this.devicesUrl;

    return this.httpClient.post<HttpResponse<any>>(url, device, {
      headers: {
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  updateDevice(id: number, deviceModel: AddEditDeviceModel): Observable<any> {
    let url: string = this.devicesUrl + "/" + id;

    return this.httpClient.put(url, deviceModel, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }

  deletedevice(id: number) {
    let url: string = this.devicesUrl + "/" + id;

    return this.httpClient.delete<HttpResponse<string>>(url, {
      headers: {
        "Content-Type": "application/json"
      },
      responseType: "text" as "json"
    });
  }

}
