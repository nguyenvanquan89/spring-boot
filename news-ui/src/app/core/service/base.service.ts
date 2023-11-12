import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RequestDTO } from '../dtos/request.dto';
import { Environment } from '../common/evironment.constants';
import { SearchDTO } from '../dtos/search.dto';
import { CommonConstants } from '../common/common.constants';

export abstract class BaseService {

  constructor(private http: HttpClient) { }

  //insert data
  protected postData(data: RequestDTO): Observable<any> {
    let url = Environment.apiBaseUrl + data.subUrl;
    return this.http.post(url, data.dataRequest, this.getHeaderConf(data));
  }

  //update data
  protected putData(data: RequestDTO): Observable<any> {
    let url = Environment.apiBaseUrl + data.subUrl;
    return this.http.put(url, data.dataRequest, this.getHeaderConf(data));
  }

  //delete data
  protected deleteData(data: RequestDTO): Observable<any> {
    let url = Environment.apiBaseUrl + data.subUrl;
    return this.http.delete(url, this.getHeaderConf(data));
  }

  //get one by id
  protected findOneById(data: RequestDTO): Observable<any> {
    let url = Environment.apiBaseUrl + data.subUrl;
    return this.http.get(url, this.getHeaderConf(data));
  }

  //get data has paging
  protected findAllByPaging(data: RequestDTO, searchDto: SearchDTO[]): Observable<any> {
    let url = Environment.apiBaseUrl + data.subUrl;
    let params = new HttpParams();
    params = params.set('language', data.language);
    searchDto.forEach(element => {
      params = params.set(element.key, element.value);
    });
    return this.http.get(url, { params });
  }

  //create header
  protected getHeaderConf(data: RequestDTO): any {
    let params = new HttpParams();
    params = params.set('language', data.language);
    let header = new HttpHeaders();
    header = header.set('Accept-Language', data.language);
    if (!data.nonContentType) {
      header = header.set('Content-Type', CommonConstants.CONTENT_TYPE_JSON);
    }
    let headerConfig =
    {
      headers: header,
      // new HttpHeaders({
      //   //'Content-Type': data.contentType? data.contentType : CommonConstants.CONTENT_TYPE_JSON,
      //   'Accept-Language': data.language,
      // }),
      params,
      body: data.dataRequest, // when delete then set ids to body
    };
    // const tem = {...headerConfig, body:data.dataRequest};
    // headerConfig = {...tem};
    return headerConfig;
  }

}
