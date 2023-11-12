import { Injectable } from '@angular/core';
import { CommonConstants } from '../common/common.constants';
import { LoginResponse } from '../dtos/login.response';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private jwtService = new JwtHelperService();
  private subjectName = new Subject<any>(); //need to create a subject

  constructor() { }

  //save data user logged in
  public saveUserLoggedIn(loginResponse: LoginResponse) {
    localStorage.removeItem(CommonConstants.TOKEN_KEY);
    localStorage.setItem(CommonConstants.TOKEN_KEY, JSON.stringify(loginResponse));
  }

  //remove user logged in
  public removeUserLoggedIn() {
    localStorage.removeItem(CommonConstants.TOKEN_KEY);
  }

  //get token
  public getToken(): string | null {
    let strUser = localStorage.getItem(CommonConstants.TOKEN_KEY);
    if (strUser) {
      let userLogin: LoginResponse = JSON.parse(strUser);
      //if token expired then remove token and return null
      if (this.jwtService.isTokenExpired(userLogin.jwtToken)) {
        this.removeUserLoggedIn();
        return null;
      }
      return userLogin.jwtToken;
    }
    return null;
  }
  //get user form token
  private getRolesFromToken(): string[] {
    let tokenData = this.jwtService.decodeToken(this.getToken() ?? '');
    if (tokenData != null && tokenData['roleCodes']) {
      return tokenData['roleCodes'];
    }
    return [];
  }

  //check permit of user logged in by expected roles
  public isPermit(expectedRoles: string[]): boolean {
    const userRoles: string[] = this.getRolesFromToken();
    let permitted: boolean = false;
    //check role of user logged in
    expectedRoles.forEach(expectRole => {
      userRoles.forEach(userRole => {
        if (expectRole.toUpperCase() == userRole.toUpperCase()) {
          permitted = true;
        }
      });
    });
    return permitted;
  }

  public updateUserName() {
    this.subjectName.next({ username: this.getUserNameFromToken() });
  }
  public getUserName(): Observable<any> { //the receiver component calls this function
    return this.subjectName.asObservable(); //it returns as an observable to which the receiver funtion will subscribe
  }

  //get user name from token
  private getUserNameFromToken(): string {
    let tokenData = this.jwtService.decodeToken(this.getToken() ?? '');
    if (tokenData != null && tokenData['sub']) {
      return tokenData['sub'];
    }
    return '';
  }

  //check logged in
  public isLoggedIn(): boolean {
    if (this.getToken()) {
      return true;
    }
    return false;
  }
}
