import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CommonConstants } from 'src/app/core/common/common.constants';
import { BaseDTO } from 'src/app/core/dtos/base.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { UserDTO } from 'src/app/core/dtos/user.dto';
import { UserService } from 'src/app/core/service/user.service';

@Component({
  selector: 'app-admin-user',
  templateUrl: './admin-user.component.html',
  styleUrls: ['./admin-user.component.css']
})
export class AdminUserComponent implements OnInit {

  currentPage: number = CommonConstants.CURRENT_PAGE;
  itemPerPage: number = CommonConstants.ITEM_PERPAGE;
  totalRecord: number = 0;
  lstResult: UserDTO[] = [];
  lstSelectId: number[] = [];
  checkAll: boolean = false;
  msgServer: string = '';
  isErrorServer: boolean = false;
  hasRoleAdmin: boolean;


  constructor(
    private authService: AuthService,
    private userService: UserService,
  ) {
    this.hasRoleAdmin = this.authService.isPermit([CommonConstants.ROLE_ADMIN]);
  }

  ngOnInit(): void {
    this.authService.updateUserName();
        //init search dto
        const searchDto = this.initCurrentDataSearchDTO();
        this.loadUserByPaging(searchDto);
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

  //Load comment by paging
  private loadUserByPaging(searchDto: SearchDTO[]) {
    this.userService.findAllUsersByPaging(searchDto).subscribe({
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
    this.loadUserByPaging(searchDto);
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

  //Delete checked Comment (only admin anh editor has role)
  deleteChecked() {
    const checkedIds = this.getCheckedItems(this.lstResult);
    this.userService.deleteUserData(checkedIds).subscribe({
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
        this.loadUserByPaging(searchDto);
        this.lstSelectId = [];
      }
    });
  }
}
