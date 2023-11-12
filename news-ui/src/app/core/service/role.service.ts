import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { BaseService } from './base.service';
import { SearchDTO } from '../dtos/search.dto';
import { RequestDTO } from '../dtos/request.dto';
import { CommonConstants } from '../common/common.constants';
import { UrlConstants } from '../common/url.constants';
import { RoleDTO } from '../dtos/role.dto';

@Injectable({
  providedIn: 'root'
})
export class RoleService extends BaseService {
  constructor(http: HttpClient) {
    super(http);
  }

  //find all role by paging
  public findAllRolesByPaging(searchDTO: SearchDTO[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.ROLES_URL,
      'dataRequest': {},
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.findAllByPaging(requestDto, searchDTO);
  }

  //find one role by id
  public findOneRoleById(searchDTO: SearchDTO[]): Observable<any> {
    let subUrl = UrlConstants.ROLES_URL;
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
  public saveRoleData(roleDto: RoleDTO): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.ROLES_URL,
      'dataRequest': roleDto,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    if (roleDto.id > 0) { // update data
      return this.putData(requestDto);
    }
    //insert data
    return this.postData(requestDto);
  }

  //delete role Data (role admin)
  public deleteRoleData(ids: number[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.ROLES_URL,
      'dataRequest': ids,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.deleteData(requestDto);
  }

}
