import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CommonConstants } from 'src/app/core/common/common.constants';
import { Environment } from 'src/app/core/common/evironment.constants';
import { UrlConstants } from 'src/app/core/common/url.constants';
import { BaseDTO } from 'src/app/core/dtos/base.dto';
import { NewsDTO } from 'src/app/core/dtos/news.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { NewsService } from 'src/app/core/service/news.service';

@Component({
  selector: 'app-admin-news',
  templateUrl: './admin-news.component.html',
  styleUrls: ['./admin-news.component.css']
})
export class AdminNewsComponent implements OnInit {

  currentPage: number = CommonConstants.CURRENT_PAGE;
  itemPerPage: number = CommonConstants.ITEM_PERPAGE;
  totalRecord: number = 0;
  lstResult: NewsDTO[] = [];
  keyword: string = '';
  categoryId: number = 0;
  lstSelectId: number[] = [];
  checkAllNews: boolean = false;
  msgServer: string = '';
  isErrorServer: boolean = false;
  hasRoleAdmin: boolean;

  constructor(
    private newsService: NewsService,
    private authService: AuthService,
  ) {
    this.hasRoleAdmin = this.authService.isPermit([CommonConstants.ROLE_ADMIN]);
  }

  ngOnInit(): void {
    this.authService.updateUserName();
    //init search dto
    const searchDto = this.initCurrentDataSearchDTO();
    this.findAllNewsByPaging(searchDto);
  }

  //init current data for search dto of home screen
  initCurrentDataSearchDTO(): SearchDTO[] {
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

  //Call news service 
  findAllNewsByPaging(searchDto: SearchDTO[]) {
    this.newsService.findAllNewsByPaging(searchDto).subscribe({
      next: (data) => {
        this.totalRecord = data.totalRecord;
        this.itemPerPage = data.itemPerPage;
        this.currentPage = data.currentPage;
        data.lstResult.forEach((item: NewsDTO) => {
          let thumbnail = item.thumbnail;
          if (thumbnail) {
            item.thumbnail = Environment.apiBaseUrl + UrlConstants.NEWS_IMAGE_URL + thumbnail;
          }
          item.isSelected = false; // set all input checkbox item not checked
        });
        this.lstResult = data.lstResult;
      },
      error: (error) => {
        console.log(error);
        this.msgServer = error.error.message;
      },
      complete: () => {
        this.authService.updateUserName();
        this.msgServer = '';
        console.log("Get news success");
      }
    });
  }

  //Change page and search news from serve
  onChangePage(page: number) {
    this.currentPage = page;
    const searchDto = this.initCurrentDataSearchDTO();
    this.findAllNewsByPaging(searchDto);
  }

  //Search news by keyword
  searchNewsByKeyword() {
    this.currentPage = CommonConstants.CURRENT_PAGE;
    this.itemPerPage = CommonConstants.ITEM_PERPAGE;
    const searchDto = this.initCurrentDataSearchDTO();
    this.findAllNewsByPaging(searchDto);
  }

  // is all checked
  selectOneItem() {
    this.lstSelectId = this.getCheckedItems(this.lstResult);
    if (this.lstResult.length == this.lstSelectId.length) {
      this.checkAllNews = true;
    } else {
      this.checkAllNews = false;
    }
  }

  //Select/Unselect all item
  selectAllItems() {
    for (let item of this.lstResult) {
      item.isSelected = this.checkAllNews;
    }
    this.lstSelectId = this.getCheckedItems(this.lstResult);
  }

  //Get checked items
  getCheckedItems(lstResult: BaseDTO[]): number[] {
    return lstResult
      .filter(item => item.isSelected)
      .map(item => item.id);
  }

  //Delete checked news (only admin has role)
  deleteCheckedNews() {
    const checkedIds = this.getCheckedItems(this.lstResult);
    this.newsService.deleteNewsData(checkedIds).subscribe({
      next: (res) => {
        this.msgServer = res.message;
      },
      error: (error) => {
        this.msgServer = error.error.message;
        this.isErrorServer = true;
      },
      complete: () => {
        this.isErrorServer = false;
        const searchDto = this.initCurrentDataSearchDTO();
        this.findAllNewsByPaging(searchDto);
      }
    });
  }

}
