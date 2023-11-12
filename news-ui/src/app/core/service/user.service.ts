import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegisterDTO } from '../dtos/register.dto';
import { LoginDTO } from '../dtos/login.dto';
import { BaseService } from './base.service';
import { RequestDTO } from '../dtos/request.dto';
import { UrlConstants } from '../common/url.constants';
import { HttpClient } from '@angular/common/http';
import { CommonConstants } from '../common/common.constants';
import { UserDTO } from '../dtos/user.dto';
import { SearchDTO } from '../dtos/search.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService extends BaseService {
  constructor(http: HttpClient) {
    super(http);
  }


  //login user
  public loginUser(loginDto: LoginDTO): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.LOGIN_URL,
      'dataRequest': loginDto,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.postData(requestDto);
  }

  //customer register user
  public customerRegisterUserAccount(registerDto: RegisterDTO): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.USERS_REGISTER_URL,
      'dataRequest': registerDto,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.postData(requestDto);
  }

  //update user (role admin)
  public udpateUser(registerDto: RegisterDTO): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.USERS_URL,
      'dataRequest': registerDto,
      'language': CommonConstants.DEFAULT_LANGUAGE,
    };
    return this.putData(requestDto);
  }


  //find all user by paging
  public findAllUsersByPaging(searchDTO: SearchDTO[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.USERS_URL,
      'dataRequest': {},
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.findAllByPaging(requestDto, searchDTO);
  }

  //find one user by id
  public findOneUserById(searchDTO: SearchDTO[]): Observable<any> {
    let subUrl = UrlConstants.USERS_URL;
    if (searchDTO[0] && searchDTO[0].key == 'id') {
      subUrl += '/' + searchDTO[0].value;
    }
    const requestDto: RequestDTO = {
      'subUrl': subUrl,
      'dataRequest': {},
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.findOneById(requestDto);
  }

  //insert or update role Data (role admin)
  public saveUserData(userDto: UserDTO): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.USERS_URL,
      'dataRequest': userDto,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    if (userDto.id > 0) { // update data
      return this.putData(requestDto);
    }
    //insert data
    const requestRegisterDto: RequestDTO = {
      'subUrl': UrlConstants.USERS_REGISTER_URL,
      'dataRequest': userDto,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.postData(requestRegisterDto);
  }

  //delete user Data (role admin)
  public deleteUserData(ids: number[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.USERS_URL,
      'dataRequest': ids,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.deleteData(requestDto);
  }

}
