import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CommentDTO } from 'src/app/core/dtos/comment.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { Comment } from 'src/app/core/entities/comment';
import { CommentService } from 'src/app/core/service/comment.service';

@Component({
  selector: 'app-admin-comment-edit',
  templateUrl: './admin-comment-edit.component.html',
  styleUrls: ['./admin-comment-edit.component.css']
})
export class AdminCommentEditComponent implements OnInit {

  commentId: string = '';
  commentDto: CommentDTO;
  commentForm: FormGroup;
  msgSuccess: string = '';
  msgErrorServer: string = '';

  constructor(
    private commentService: CommentService,
    private activedRoute: ActivatedRoute,
    private authService: AuthService,
    private fb: FormBuilder,
  ) {
    this.commentDto = this.initDto();
    this.commentForm = this.initForm();
  }

  ngOnInit(): void {
    this.activedRoute.params.subscribe(
      params => {
        console.log("comment id = " + params['commentId']);
        if (params['commentId']) {
          this.commentId = params['commentId'];
        }
      });
    this.getCommentById();
  }


  //init Comment DTO
  private initDto() {
    let data = {
      'id': '',
      'content': '',
      'user': { 'id': '' },
      'news': {
        'id': '',
        'category': {
          'id': '',
          'name': '',
        },
      },
    };
    return new CommentDTO(data);
  }

  //Init Comment form
  private initForm(initData?: any) {
    return this.fb.group({
      id: [initData?.id],
      content: [initData?.content, [Validators.required, Validators.minLength(20)]],
      userId: [initData?.user.id],
      newsId: [initData?.news.id],
    });
  }

  //load comment by id
  private getCommentById() {
    //if insert 
    if (this.commentId == '') {
      return;
    }
    //create search DTO
    const searchDto: SearchDTO[] = [
      {
        'key': 'id',
        'value': this.commentId
      }
    ];
    //call service to find by id
    this.commentService.findOneCommentById(searchDto).subscribe({
      next: (data: Comment) => {
        this.commentDto = { ...data };
        this.commentForm = this.initForm(this.commentDto);
      },
      error: (error) => {
        this.msgErrorServer = error.error.message;
      },
      complete: () => {
        this.msgErrorServer = '';
        this.authService.updateUserName();
      }
    })
  }

  //Save data to database
  saveData() {
    //Exclude userId and newsId properties
    const { userId, newsId, ...tmpForm } = this.commentForm.value;
    //use spread() operator to copy properties from tempForm to newsDto 
    this.commentDto = {
      ...this.commentDto,
      ...tmpForm
    }

    //if have id then update or insert
    this.commentService.saveCommentData(this.commentDto).subscribe({
      next: (res) => {
        this.commentDto = { ...res.data };
        this.commentForm = this.initForm(this.commentDto);
        this.msgSuccess = res.message;
        this.msgErrorServer = '';
      },
      error: (error) => {
        this.msgErrorServer = error.error.message;
        this.msgSuccess = '';
      },
    });
  }

}
