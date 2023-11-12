import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CategoryDTO } from 'src/app/core/dtos/category.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { Category } from 'src/app/core/entities/category';
import { CategoryService } from 'src/app/core/service/category.service';

@Component({
  selector: 'app-admin-category-edit',
  templateUrl: './admin-category-edit.component.html',
  styleUrls: ['./admin-category-edit.component.css']
})
export class AdminCategoryEditComponent implements OnInit {
  categoryId: string = '';
  categotyDto: CategoryDTO;
  categoryForm: FormGroup;
  msgSuccess: string = '';
  msgErrorServer: string = '';

  constructor(
    private categoryService: CategoryService,
    private activedRoute: ActivatedRoute,
    private authService: AuthService,
    private fb: FormBuilder,
  ) {
    this.categotyDto = this.initDto();
    this.categoryForm = this.initForm();
  }

  ngOnInit(): void {
    this.activedRoute.params.subscribe(
      params => {
        console.log("category id = " + params['categoryId']);
        if (params['categoryId']) {
          this.categoryId = params['categoryId'];
        }
      }
    );
    this.getCategoryById();
  }


  //init Category DTO
  private initDto() {
    let data = {
      'id': '',
      'name': '',
      'code': '',
    };
    return new CategoryDTO(data);
  }

  //Init category form
  private initForm(initData?: any) {
    return this.fb.group({
      id: [initData?.id],
      name: [initData?.name, [Validators.required, Validators.minLength(5)]],
      code: [initData?.code, [Validators.required, Validators.minLength(5)]],
    });
  }

  //load category by id
  private getCategoryById() {
    //if insert news 
    if (this.categoryId == '') {
      return;
    }
    //create search DTO
    const searchDto: SearchDTO[] = [
      {
        'key': 'id',
        'value': this.categoryId
      }
    ];
    //call service to find category by id
    this.categoryService.findOneCategoryById(searchDto).subscribe({
      next: (data: Category) => {
        this.categotyDto = { ...data };
        this.categoryForm = this.initForm(this.categotyDto);
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
    //use spread() operator to copy properties from categoryForm to categotyDto 
    this.categotyDto = {
      ...this.categotyDto,
      ...this.categoryForm.value
    }
    //if have id then update or insert
    this.categoryService.saveCategoryData(this.categotyDto).subscribe({
      next: (res) => {
        this.categotyDto = { ...res.data };
        this.categoryForm = this.initForm(this.categotyDto);
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
