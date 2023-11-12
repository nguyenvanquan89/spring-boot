import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
//import { SimpleUploadAdapter } from '@ckeditor/ckeditor5-upload';

import CkEditorCustom from 'ckeditor5_custom/build/ckeditor';

import { AuthService } from 'src/app/core/auth/auth.service';
import { CommonConstants } from 'src/app/core/common/common.constants';
import { Environment } from 'src/app/core/common/evironment.constants';
import { UrlConstants } from 'src/app/core/common/url.constants';
import { CategoryDTO } from 'src/app/core/dtos/category.dto';
import { NewsDTO } from 'src/app/core/dtos/news.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { News } from 'src/app/core/entities/news';
import { CategoryService } from 'src/app/core/service/category.service';
import { NewsService } from 'src/app/core/service/news.service';

@Component({
    selector: 'app-admin-news-edit',
    templateUrl: './admin-news-edit.component.html',
    styleUrls: ['./admin-news-edit.component.css']
})
export class AdminNewsEditComponent implements OnInit {
    newsId: string = '';
    newsDto: NewsDTO;
    categories: CategoryDTO[] = [];
    newsForm: FormGroup;
    selectedFile: any;
    msgSuccess: string = '';
    msgErrorServer: string = '';
    //ckeditor
    Editor = CkEditorCustom;
    //config ckeditor
    editorConfig : any;

    constructor(
        private categoryService: CategoryService,
        private newsService: NewsService,
        private activedRoute: ActivatedRoute,
        private authService: AuthService,
        private fb: FormBuilder,
    ) {
        this.newsDto = this.initNewsDto();
        this.newsForm = this.initNewsForm();
        this.editorConfig = this.iniConfigCkeditor();
    }

    ngOnInit(): void {
        this.activedRoute.params.subscribe(
            params => {
                if (params['newsId']) {
                    this.newsId = params['newsId'];
                }
            }
        );
        this.getNewsById();
        this.loadCategory();
    }

    //init config of ckeditor
    private iniConfigCkeditor() {
        return {
            placeholder: 'Type the content here!',
            //plugins: [ SimpleUploadAdapter, /* ... */ ],
            simpleUpload: {
                // The URL that the images are uploaded to.
                uploadUrl: Environment.apiBaseUrl+UrlConstants.NEWS_IMAGE_UPLOAD,
                 // Enable the XMLHttpRequest.withCredentials property.
                 withCredentials: true,
    
                 // Headers sent along with the XMLHttpRequest to the upload server.
                 headers: {
                     'X-CSRF-TOKEN': 'CSRF-Token',
                     Authorization: 'Bearer ' + this.authService.getToken(),
                 }
            },
            language: 'en',
        };
    }

    //get news by id
    private getNewsById() {
        //if insert news 
        if (this.newsId == '') {
            return;
        }
        const searchDto: SearchDTO[] = [
            {
                'key': 'id',
                'value': this.newsId
            }
        ];
        this.newsService.findOneNewsById(searchDto).subscribe({
            next: (data: News) => {
                this.newsDto = { ...data };
                this.newsForm = this.initNewsForm(this.newsDto);
            },
            error: (error) => {
                console.log(error.error.message);
                this.msgErrorServer = error.error.message;
            },
            complete: () => {
                console.log("Loaded news to edit id=" + this.newsId);
                this.msgErrorServer = '';
                this.authService.updateUserName();
            },
        });
    }

    //Load category
    private loadCategory() {
        const searchDto: SearchDTO[] = [
            {
                'key': 'currentPage',
                'value': CommonConstants.CURRENT_PAGE
            },
            {
                'key': 'itemPerPage',
                'value': CommonConstants.ITEM_PERPAGE
            }
        ];
        this.categoryService.findAllCategoriesByPaging(searchDto).subscribe({
            next: (data) => {
                this.categories = data.lstResult;
            },
            error: (error) => {
                console.log(error);
                this.msgErrorServer = error.error.message;
            },
            complete: () => {
                this.msgErrorServer = '';
                console.log("Load category success");
            }
        });
    }

    //Init news form
    private initNewsForm(initData?: any) {
        return this.fb.group({
            id: [initData?.id],
            title: [initData?.title, [Validators.required, Validators.minLength(10)]],
            thumbnail: [initData?.thumbnail],
            categoryId: [initData?.category.id],
            content: [initData?.content, [Validators.required, Validators.minLength(100)]],
            shortDescription: [initData?.shortDescription, [Validators.required, Validators.minLength(100)]],
        });
    }

    //init news DTO
    private initNewsDto() {
        let data = {
            'id': '',
            'content': '',
            'title': '',
            'thumbnail': '',
            'shortDescription': '',
            'category': { 'id': '' }
        };
        return new NewsDTO(data);
    }

    //event select file image
    onFileSelected(event: any) {
        if (event.target.files.length > 0) {
            const file = event.target.files[0];
            this.selectedFile = file;
        }
    }

    //save data to database
    saveNews() {
        //Exclude categoryId properties
        const { categoryId, ...tmpForm } = this.newsForm.value;
        //use spread() operator to copy properties from tempForm to newsDto 
        this.newsDto = {
            ...this.newsDto,
            ...tmpForm
        }
        //set category id new
        this.newsDto.category.id = this.newsForm.value['categoryId'];
        const formData: FormData = new FormData();
        formData.append('file', this.selectedFile);
        formData.append('data', new Blob([JSON.stringify(this.newsDto)], { 'type': CommonConstants.CONTENT_TYPE_JSON }));

        //insert or update data to database
        this.newsService.saveNewsData(formData, this.newsDto.id).subscribe({
            next: (res) => {
                this.newsDto = { ...res.data };
                this.newsForm = this.initNewsForm(this.newsDto);
                this.msgSuccess = res.message;
                this.msgErrorServer = '';
            },
            error: (error) => {
                this.msgErrorServer = error.error.message;
                this.msgSuccess = '';
            }
        });
    }
}