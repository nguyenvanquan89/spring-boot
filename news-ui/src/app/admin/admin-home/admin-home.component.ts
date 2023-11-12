import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DatePipe } from '@angular/common';
import { AuthService } from 'src/app/core/auth/auth.service';
import { NewsService } from 'src/app/core/service/news.service';
import { CommonConstants } from 'src/app/core/common/common.constants';
import { RoleService } from 'src/app/core/service/role.service';
import { UserService } from 'src/app/core/service/user.service';
import { CommentService } from 'src/app/core/service/comment.service';
import { CategoryService } from 'src/app/core/service/category.service';
import { NewsDTO } from 'src/app/core/dtos/news.dto';
import { CommentDTO } from 'src/app/core/dtos/comment.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {

  hasRoleAdmin: boolean;
  hasRoleAuthor: boolean;
  hasRoleEditor: boolean;

  currentPage: number = 1;
  itemPerPage: number = 10;
  lstNews: NewsDTO[] = [];
  totalNews: number = 0;

  lstComment: CommentDTO[] = [];
  totalComment: number = 0;

  orderColumn: string = 'modifiedDate';



  constructor(
    //private datePipe: DatePipe,
    private router: Router,
    private authService: AuthService,
    private roleService: RoleService,
    private newsService: NewsService,
    private userService: UserService,
    private commentService: CommentService,
    private categoryService: CategoryService,
  ) {
    this.hasRoleAdmin = this.authService.isPermit([CommonConstants.ROLE_ADMIN]);
    this.hasRoleAuthor = this.authService.isPermit([CommonConstants.ROLE_AUTHOR]);
    this.hasRoleEditor = this.authService.isPermit([CommonConstants.ROLE_EDITOR]);
  }

  ngOnInit(): void {
    this.authService.updateUserName();
    const searchDto = this.initCurrentDataSearchDTO();
    this.loadTopNewsLastModified(searchDto);
    this.loadTopCommentLastModified(searchDto);
  }

  //Load top news last modified
  loadTopNewsLastModified(searchDto: SearchDTO[]) {
    this.newsService.findAllNewsByPaging(searchDto).subscribe({
      next: (res) => {
        this.totalNews = res.totalRecord;
        this.lstNews = res.lstResult;
      },
      error:(error)=> {
        console.log(error.error.message);
      },
      complete: () => {
        console.log("Home admin load top NEWS success");
      }
    })
  }

  //Load top commet last modified
  loadTopCommentLastModified(searchDto: SearchDTO[]) {
    this.commentService.findAllCommentByPaging(searchDto).subscribe({
      next:(res) => {
        this.totalComment = res.totalRecord;
        this.lstComment = res.lstResult;
      },
      error:(error) => {
        console.log(error.error.message);
      },
      complete:()=> {
        console.log("Home admin load top COMMENT success");
      }
    });
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
        'key': 'orderColumn',
        'value': this.orderColumn,
      },
    ];
    return searchDto;
  }

}
