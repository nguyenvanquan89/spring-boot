import { NgModule } from '@angular/core';
import {DatePipe, CommonModule} from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';
import { UrlFriendllyPipe } from './core/common/url.friendlly.pipe';

import { HomeComponent } from './web/home/home.component';
import { HeaderComponent } from './web/header/header.component';
import { FooterComponent } from './web/footer/footer.component';
import { DetailNewsComponent } from './web/detail-news/detail-news.component';
import { LoginComponent } from './login/login.component';

import { TokenInterceptor } from './core/interceptors/token.interceptor';
import { AppRoutingModule } from './app.routing.module';
import { AppComponent } from './app.component';
import { ErrorPageComponent } from './errors/error-page.component';
import { AuthGuard } from './core/auth/auth.guard';

import { AdminComponent } from './admin/admin.component';
import { AdminHeaderComponent } from './admin/admin-header/admin-header.component';
import { AdminFooterComponent } from './admin/admin-footer/admin-footer.component';
import { AdminCategoryComponent } from './admin/admin-category/admin-category.component';
import { AdminCommentComponent } from './admin/admin-comment/admin-comment.component';
import { AdminRolesComponent } from './admin/admin-roles/admin-roles.component';
import { AdminUserComponent } from './admin/admin-user/admin-user.component';

import { AdminNewsComponent } from './admin/admin-news/admin-news.component';
import { AdminNewsEditComponent } from './admin/admin-news/admin-news-edit.component';
import { AdminRoutingModule } from './admin/admin.routing.module';
import { AdminHomeComponent } from './admin/admin-home/admin-home.component';
import { WordTrimPipe } from './core/common/word.trim.pipe';
import { AdminCategoryEditComponent } from './admin/admin-category/admin-category-edit.component';
import { AdminCommentEditComponent } from './admin/admin-comment/admin-comment-edit.component';
import { AdminRoleEditComponent } from './admin/admin-roles/admin-role-edit.component';
import { AdminUserEditComponent } from './admin/admin-user/admin-user-edit.component';
import { CKEditorModule } from '@ckeditor/ckeditor5-angular';


@NgModule({
  declarations: [
    HeaderComponent,
    FooterComponent,
    DetailNewsComponent,
    LoginComponent,
    HomeComponent,
    AppComponent,
    UrlFriendllyPipe,
    ErrorPageComponent,
    WordTrimPipe,
    
    //Admin component
    AdminComponent,
    AdminHeaderComponent,
    AdminFooterComponent,
    AdminCategoryComponent,
    AdminCommentComponent,
    AdminRolesComponent,
    AdminUserComponent,
    AdminNewsComponent,
    AdminNewsEditComponent,
    AdminHomeComponent,
    AdminCategoryEditComponent,
    AdminCommentEditComponent,
    AdminRoleEditComponent,
    AdminUserEditComponent,

  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    ReactiveFormsModule,
    AppRoutingModule,
    AdminRoutingModule,
    CKEditorModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true,
    },
    //{ provide: 'UploadAdapter', useValue: Base64UploadAdapter },
    DatePipe,
    AuthGuard,
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
