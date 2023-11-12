import { Injectable,inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AuthService } from './auth.service';

@Injectable()
export class AuthGuard {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    let permitted : boolean = false;
    if(this.authService.isLoggedIn()) {
      const expectedRoles: string[] = route.data['expectedRole'];
      permitted = this.authService.isPermit(expectedRoles);
    }
    if(permitted) {
      return true;
    }
    this.router.navigate(['/errors-page', '403']);
    return false;
  }

}

export const authGuard: CanActivateFn = (route, state) => {
  return inject(AuthGuard).canActivate(route, state);
};