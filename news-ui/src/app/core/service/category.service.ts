import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { SearchDTO } from '../dtos/search.dto';
import { RequestDTO } from '../dtos/request.dto';
import { UrlConstants } from '../common/url.constants';
import { Observable } from 'rxjs';
import { CommonConstants } from '../common/common.constants';
import { CategoryDTO } from '../dtos/category.dto';

@Injectable({
  providedIn: 'root'
})
export class CategoryService extends BaseService {
  constructor(http: HttpClient) {
    super(http);
  }

  //find all category by paging (all)
  public findAllCategoriesByPaging(searchDTO: SearchDTO[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.CATEGORIES_URL,
      'dataRequest': {},
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.findAllByPaging(requestDto, searchDTO);
  }

  //find one category by id (all)
  public findOneCategoryById(searchDTO: SearchDTO[]): Observable<any> {
    let subUrl = UrlConstants.CATEGORIES_URL;
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

  //insert or update category Data (role: author, editor, admin)
  public saveCategoryData(categoryDto: CategoryDTO): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.CATEGORIES_URL,
      'dataRequest': categoryDto,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    if (categoryDto.id > 0) { // update data
      return this.putData(requestDto);
    }
    //insert data
    return this.postData(requestDto);

  }

  //delete category Data (role admin)
  public deleteCategoryData(ids: number[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.CATEGORIES_URL,
      'dataRequest': ids,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.deleteData(requestDto);
  }

}
