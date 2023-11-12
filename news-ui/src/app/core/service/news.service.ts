import { Injectable } from '@angular/core';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RequestDTO } from '../dtos/request.dto';
import { UrlConstants } from '../common/url.constants';
import { SearchDTO } from '../dtos/search.dto';
import { CommonConstants } from '../common/common.constants';
import { NewsDTO } from '../dtos/news.dto';

@Injectable({
  providedIn: 'root'
})
export class NewsService extends BaseService {

  constructor(http: HttpClient) {
    super(http);
  }

  //find all news by paging
  public findAllNewsByPaging(searchDTO: SearchDTO[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.NEWS_SEARCH_URL_HAS_KEY_WORD,
      'dataRequest': {},
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.findAllByPaging(requestDto, searchDTO);
  }

  //find one news by id
  public findOneNewsById(searchDTO: SearchDTO[]): Observable<any> {
    let subUrl = UrlConstants.NEWS_URL;
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

  //insert or update news Data (role admin, author, editor)
  public saveNewsData(formData: FormData, newsId: number): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.NEWS_URL,
      'dataRequest': formData,
      'language': CommonConstants.DEFAULT_LANGUAGE,
      'nonContentType': true, // non content type because upload image and json data
    };
    if (newsId > 0) {//update data
      return this.putData(requestDto);
    }
    //insert data
    return this.postData(requestDto);
  }

  //delete news Data (role admin)
  public deleteNewsData(ids: number[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.NEWS_URL,
      'dataRequest': ids,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.deleteData(requestDto);
  }

}
