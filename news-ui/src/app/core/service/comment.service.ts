import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { BaseService } from './base.service';
import { HttpClient } from '@angular/common/http';
import { RequestDTO } from '../dtos/request.dto';
import { UrlConstants } from '../common/url.constants';
import { CommonConstants } from '../common/common.constants';
import { CommentDTO } from '../dtos/comment.dto';
import { SearchDTO } from '../dtos/search.dto';

@Injectable({
  providedIn: 'root'
})
export class CommentService extends BaseService {
  constructor(http: HttpClient) {
    super(http);
  }

  //find all comment by paging
  public findAllCommentByPaging(searchDTO: SearchDTO[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.COMMENTS_URL,
      'dataRequest': {},
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.findAllByPaging(requestDto, searchDTO);
  }

  //find one comment by id
  public findOneCommentById(searchDTO: SearchDTO[]): Observable<any> {
    let subUrl = UrlConstants.COMMENTS_URL;
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

  //insert or update comment Data (all user)
  public saveCommentData(commentDto: CommentDTO): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.COMMENTS_URL,
      'dataRequest': commentDto,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    if (commentDto.id > 0) {//update data
      return this.putData(requestDto);
    }
    //insert data
    return this.postData(requestDto);
  }

  //delete comment Data (role admin)
  public deleteCommentData(ids: number[]): Observable<any> {
    const requestDto: RequestDTO = {
      'subUrl': UrlConstants.COMMENTS_URL,
      'dataRequest': ids,
      'language': CommonConstants.DEFAULT_LANGUAGE
    };
    return this.deleteData(requestDto);
  }
}
