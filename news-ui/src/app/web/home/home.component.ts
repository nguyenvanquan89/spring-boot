import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { NewsDTO } from 'src/app/core/dtos/news.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { NewsService } from 'src/app/core/service/news.service';
import { Environment } from 'src/app/core/common/evironment.constants';
import { UrlConstants } from 'src/app/core/common/url.constants';
import { CommonConstants } from 'src/app/core/common/common.constants';
import { AuthService } from 'src/app/core/auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  currentPage: number = CommonConstants.CURRENT_PAGE;
  itemPerPage: number = CommonConstants.ITEM_PERPAGE;
  totalRecord: number = 0;
  lstResult: NewsDTO[] = [];
  keyword: string = '';
  categoryId: number = 0;

  constructor(
    private newsService: NewsService,
    //private datePipe: DatePipe,
    private router: Router,
    private activedRoute: ActivatedRoute,
    private authService: AuthService,
  ) { }

  ngOnInit(): void {
    this.checkActivedRouteNewsSearch();
    //init search dto
    const searchDto = this.initCurrentDataSearchDTOForHome();
    this.findAllNewsByPaging(searchDto);
  }
  //check actived screen home and then user click category of news
  checkActivedRouteNewsSearch() {
    this.activedRoute.params.subscribe(
      params => {
        if (params['categoryId']) {
          this.categoryId = params['categoryId'] ?? '';
          this.searchNewsByKeyword();
        }
      }
    );
  }

  //init current data for search dto of home screen
  initCurrentDataSearchDTOForHome(): SearchDTO[] {
    let searchDto: SearchDTO[] = [
      {
        'key': 'currentPage',
        'value': this.currentPage
      },
      {
        'key': 'itemPerPage',
        'value': this.itemPerPage
      },
      {
        'key': 'categoryId',
        'value': this.categoryId
      },
      {
        'key': 'keyword',
        'value': this.keyword
      },
    ];
    return searchDto;
  }

  //Change page and search news from serve
  onChangePage(page: number) {
    this.currentPage = page;
    const searchDto = this.initCurrentDataSearchDTOForHome();
    this.findAllNewsByPaging(searchDto);
  }

  //Search news by keyword
  searchNewsByKeyword() {
    this.currentPage = CommonConstants.CURRENT_PAGE;
    this.itemPerPage = CommonConstants.ITEM_PERPAGE;
    const searchDto = this.initCurrentDataSearchDTOForHome();
    this.findAllNewsByPaging(searchDto);
  }

  //Call news service 
  findAllNewsByPaging(searchDto: SearchDTO[]) {
    this.newsService.findAllNewsByPaging(searchDto).subscribe({
      next: (data) => {
        this.itemPerPage = data.itemPerPage;
        this.totalRecord = data.totalRecord;
        this.currentPage = data.currentPage;
        data.lstResult.forEach((item: NewsDTO) => {
          let thumbnail = item.thumbnail;
          if (!thumbnail) {
            thumbnail = 'dummy-400X400.webp';
          }
          item.thumbnail = Environment.apiBaseUrl + UrlConstants.NEWS_IMAGE_URL + thumbnail;
        });
        this.lstResult = data.lstResult;
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        this.authService.updateUserName();
        console.log("Get news success");
      }
    });
  }

}