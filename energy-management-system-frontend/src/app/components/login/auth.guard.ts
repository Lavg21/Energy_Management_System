import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import {JWTPayload, UserService} from "../../services/user-service";
import {TokenService} from "../../services/token.service";

@Injectable({providedIn: 'root'})
export class AuthGuard implements CanActivate {

  constructor(private userService: UserService, private tokenService: TokenService, private router: Router) {

  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const decoded = this.tokenService.decode();
    if (decoded == null) {
      this.router.createUrlTree(['/login']);
      return false;
    } else {
      if (decoded.role === "ROLE_ADMIN") {
        return true;
      }

      this.router.createUrlTree(['/login']);
      return false;
    }
  }

  // canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
  //   return this.userService.loggedUser.pipe(take(1), map(user => {
  //     const isAuth = !!user;
  //
  //     if (isAuth) {
  //       const payload = jwtDecode(user.token!) as JWTPayload;
  //
  //       if (payload.scope === "ROLE_ADMIN") {
  //         return true;
  //       }
  //     }
  //
  //     return this.router.createUrlTree(["/login"]);
  //   }));
  // }
}
