import {Injectable} from '@angular/core';
import {JwtHelperService} from "@auth0/angular-jwt";

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor(private jwt: JwtHelperService) {
  }

  decode() {
    try {
      const token = sessionStorage.getItem('access_token');
      if (token == null)
        return null;
      return this.jwt.decodeToken(token);
    } catch (Error) {
      return null;
    }
  }
}
