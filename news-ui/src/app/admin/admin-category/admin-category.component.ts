import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CommonConstants } from 'src/app/core/common/common.constants';
import { BaseDTO } from 'src/app/core/dtos/base.dto';
import { CategoryDTO } from 'src/app/core/dtos/category.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { CategoryService } from 'src/app/core/service/category.service';

@Component({
  selector: 'app-admin-category',
  templateUrl: './admin-category.component.html',
  styleUrls: ['./admin-category.component.css']
})
export class AdminCategoryComponent implements OnInit {
  currentPage: number = CommonConstants.CURRENT_PAGE;
  itemPerPage: number = CommonConstants.ITEM_PERPAGE;
  totalRecord: number = 0;
  lstResult: CategoryDTO[] = [];
  lstSelectId: number[] = [];
  checkAll: boolean = false;
  msgServer: string = '';
  isErrorServer: boolean = false;
  hasRoleAdmin: boolean;
  hasRoleEditor: boolean;

  constructor(
    private authService: AuthService,
    private categoryService: CategoryService,
  ) {
    this.hasRoleAdmin = this.authService.isPermit([CommonConstants.ROLE_ADMIN]);
    this.hasRoleEditor = this.authService.isPermit([CommonConstants.ROLE_EDITOR]);
  }

  ngOnInit(): void {
    this.authService.updateUserName();
    //init search dto
    const searchDto = this.initCurrentDataSearchDTO();
    this.loadCategoryByPaging(searchDto);
  }
  //init current data for search dto of home screen
  private initCurrentDataSearchDTO(): SearchDTO[] {
    let searchDto: SearchDTO[] = [
      {
        'key': 'currentPage',
        'value': this.currentPage
      },
      {
        'key': 'itemPerPage',
        'value': this.itemPerPage
      },
    ];
    return searchDto;
  }

  //Load category by paging
  private loadCategoryByPaging(searchDto: SearchDTO[]) {
    this.categoryService.findAllCategoriesByPaging(searchDto).subscribe({
      next: (data) => {
        this.totalRecord = data.totalRecord;
        this.itemPerPage = data.itemPerPage;
        this.currentPage = data.currentPage;
        this.lstResult = data.lstResult;
      },
      error: (error) => {
        this.isErrorServer = true;
        this.msgServer = error.error.message;
      },
      complete: () => {
        this.authService.updateUserName();
      }
    });
  }

  //Change page and search news from serve
  onChangePage(page: number) {
    this.currentPage = page;
    const searchDto = this.initCurrentDataSearchDTO();
    this.loadCategoryByPaging(searchDto);
  }

  // is all checked
  selectOneItem() {
    this.lstSelectId = this.getCheckedItems(this.lstResult);
    if (this.lstResult.length == this.lstSelectId.length) {
      this.checkAll = true;
    } else {
      this.checkAll = false;
    }
  }

  //Select/Unselect all item
  selectAllItems() {
    for (let item of this.lstResult) {
      item.isSelected = this.checkAll;
    }
    this.lstSelectId = this.getCheckedItems(this.lstResult);
  }

  //Get checked items
  private getCheckedItems(lstResult: BaseDTO[]): number[] {
    return lstResult
      .filter(item => item.isSelected)
      .map(item => item.id);
  }

  //Delete checked category (only admin anh editor has role)
  deleteChecked() {
    const checkedIds = this.getCheckedItems(this.lstResult);
    this.categoryService.deleteCategoryData(checkedIds).subscribe({
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
        this.loadCategoryByPaging(searchDto);
        this.lstSelectId = [];
      }
    });
  }

}