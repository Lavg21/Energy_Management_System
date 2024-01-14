import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable, tap} from "rxjs";
import {LoggedInUserModel} from "../models/logged-in-user.model";
import {Router} from "@angular/router";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import jwtDecode from "jwt-decode";
import {AddUserModel} from "../models/add-user.model";
import {EditUserModel} from "../models/edit-user.model";
import {UserModel} from "../models/user.model";
import {LoginResponseModel} from "../models/login-response.model";
import {LoginRequestModel} from "../models/login-request.model";

export interface JWTPayload {
  exp: number;
  iat: number;
  iss: string;
  scope: string;
  sub: string;
}

@Injectable({
  providedIn: 'root'
})

export class UserService {

  loggedUser = new BehaviorSubject<LoggedInUserModel>(null!);

  loginUrl: string = "http://localhost:8080/user/login";

  usersUrl: string = "http://localhost:8080/user";

  private tokenExpirationTimer: any;

  constructor(private httpClient: HttpClient, private router: Router) {

  }

  getUserByToken() {
    let url: string = this.usersUrl;
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.get<HttpResponse<any>>(url + "/token", {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  getUserById(id: number) {
    let url: string = this.usersUrl + "/" + id;
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.get<HttpResponse<any>>(url, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  getAllUsers() {
    let url: string = this.usersUrl + "/all";
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.get<HttpResponse<any>>(url, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  login(email: string, password: string): Observable<LoginResponseModel> {
    let user: LoginRequestModel = new LoginRequestModel(email, password);
    return this.httpClient.post<LoginResponseModel>(this.loginUrl, user);
    // const httpOptions = {
    //   headers: new HttpHeaders({
    //     "Content-Type": "application/json",
    //     "Authorization": "Basic " + btoa(email + ":" + password)
    //   }),
    //   observe: "response" as "body"
    // };
    //
    // return this.httpClient.post<HttpResponse<any>>(this.loginUrl, null, httpOptions).pipe(tap(responseData => {
    //   const authHeader = String(String(responseData.headers.get("Authorization")) || '');
    //
    //   if (authHeader.startsWith("Bearer ")) {
    //     const token = authHeader.substring(7, authHeader.length);
    //     const payload = jwtDecode(token) as JWTPayload;
    //
    //     this.handleAuthentication(token, payload.exp - payload.iat);
    //   }
    // }))
  }
  //
  // autoLogin() {
  //   const token = sessionStorage.getItem("access_token")!;
  //
  //   if (!token) {
  //     return;
  //   }
  //   const payload = jwtDecode(token) as JWTPayload;
  //   const tokenExpiresIn = payload.exp - payload.iat;
  //   const tokenExpirationDate = new Date(new Date().getTime() + tokenExpiresIn * 1000);
  //   const loadedUser = new LoggedInUserModel(token, tokenExpirationDate);
  //
  //   if (loadedUser.token) {
  //     this.loggedUser.next(loadedUser);
  //     const expirationDuration = tokenExpirationDate.getTime() - new Date().getTime();
  //     this.autoLogout(expirationDuration);
  //   }
  // }

  logout() {
    this.loggedUser.next(null!);
    this.router.navigate(["/login"])
      .then(() => {
        window.location.reload();
      });
    localStorage.removeItem("token");

    if (this.tokenExpirationTimer) {
      clearTimeout(this.tokenExpirationTimer);
    }
    this.tokenExpirationTimer = null;
  }

  autoLogout(expirationDuration: number) {
    this.tokenExpirationTimer = setTimeout(() => {
        this.logout();
      },
      expirationDuration);
  }

  updateUser(id: number, userModel: EditUserModel): Observable<any> {
    let url = this.usersUrl + "/" + id;
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.put(url, userModel, {
      headers: {
        'Authorization': "Bearer " + token,
        'Content-Type': 'application/json'
      }
    });
  }

  addUser(user: AddUserModel) {
    let url = this.usersUrl;
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.post<HttpResponse<any>>(url, user, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      observe: "response" as "body"
    });
  }

  deleteUser(id: number) {
    let url = this.usersUrl + "/" + id;
    let token = sessionStorage.getItem("access_token");

    return this.httpClient.delete<HttpResponse<string>>(url, {
      headers: {
        "Authorization": "Bearer " + token,
        "Content-Type": "application/json"
      },
      responseType: "text" as "json"
    });
  }

}
