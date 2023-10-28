import {Injectable} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {AddEditDeviceModel} from "../models/add-edit-device.model";
import {BehaviorSubject, Observable} from "rxjs";
import {LoggedInUserModel} from "../models/logged-in-user.model";
import {MappingModel} from "../models/mapping.model";

@Injectable({
  providedIn: 'root'
})


export class DeviceService {

  loginUrl: string = "http://localhost:8080/login";
  devicesUrl: string = "http://localhost:8081/device";

  loggedUser = new BehaviorSubject<LoggedInUserModel>(null!);

  private tokenExpirationTimer: any;


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

  getAllUnmappedDevices() {
    let url: string = this.devicesUrl + "/unmapped";

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

  deleteDevice(id: number) {
    let url: string = this.devicesUrl + "/" + id;

    return this.httpClient.delete<HttpResponse<string>>(url, {
      headers: {
        "Content-Type": "application/json"
      },
      responseType: "text" as "json"
    });
  }

  getAllMappings() {
    let url: string = this.devicesUrl + "/user/all";
    let token = localStorage.getItem("token");

    return this.httpClient.get<HttpResponse<any>>(url, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  addMapping(mapping: MappingModel) {
    let url: string = this.devicesUrl + "/mapping";

    return this.httpClient.post<HttpResponse<any>>(url, mapping, {
      headers: {
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  deleteMapping(id: number) {
    let url: string = this.devicesUrl + "/mapping/" + id;

    return this.httpClient.delete<HttpResponse<string>>(url, {
      headers: {
        "Content-Type": "application/json"
      },
      responseType: "text" as "json"
    });
  }

}
