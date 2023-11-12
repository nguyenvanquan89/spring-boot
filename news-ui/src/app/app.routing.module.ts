import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from '@angular/router';
import { HomeComponent } from './web/home/home.component';
import { LoginComponent } from './login/login.component';
import { DetailNewsComponent } from './web/detail-news/detail-news.component';
import { AdminComponent } from './admin/admin.component';
import { ErrorPageComponent } from './errors/error-page.component';
import { authGuard } from './core/auth/auth.guard';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AdminNewsComponent } from './admin/admin-news/admin-news.component';
import { AdminRolesComponent } from './admin/admin-roles/admin-roles.component';
import { AdminCategoryComponent } from './admin/admin-category/admin-category.component';
import { AdminCommentComponent } from './admin/admin-comment/admin-comment.component';
import { AdminUserComponent } from './admin/admin-user/admin-user.component';
import { AdminNewsEditComponent } from './admin/admin-news/admin-news-edit.component';

const routes : Routes = [
//Web url
{path: '', component: HomeComponent},
{path: 'login', component : LoginComponent},
{path: 'register', component: LoginComponent},
{
    path: 'news-search/:categoryCode/:categoryId/view.html', 
    component: HomeComponent, 
    pathMatch:'full'
},
{
    path: 'news-detail/:categoryCode/:newsTitle/:categoryId/:newsId/view.html', 
    component: DetailNewsComponent, 
    pathMatch:'full'
},

//error page
{path: 'errors-page/:errorCode', component: ErrorPageComponent},

//{path: '**', component: ErrorPageComponent},

//Admin url
{
    path: 'admin', 
    component : AdminComponent, 
    canActivate: [authGuard],
    data: { 
        expectedRole: ['ADMIN', 'AUTHOR', 'EDITOR']
    },
    // children: [
    //     {
    //         path:'news/:view', 
    //         component: AdminNewsComponent,
    //     },
    //     {
    //         path:'news/edit/:title/:newsId/:view', 
    //         component: AdminNewsEditComponent,
    //     },
    //     {
    //         path:'news/add/:view', 
    //         component: AdminNewsEditComponent,
    //     },
    //     {
    //         path:'categories/:view', 
    //         component: AdminCategoryComponent,
    //     },
    //     {
    //         path:'comments/:view', 
    //         component: AdminCommentComponent,
    //     },
    //     {
    //         path:'roles/:view', 
    //         component: AdminRolesComponent,
    //         canActivate: [authGuard],
    //         data: {
    //             expectedRole: ['ADMIN']
    //         },
    //     },
    //     {
    //         path:'users/:view', 
    //         component: AdminUserComponent,
    //         canActivate: [authGuard],
    //         data: {
    //             expectedRole: ['ADMIN']
    //         },
    //     },
    // ],
}
    
];

@NgModule({
    imports: [
        RouterModule.forRoot(routes),
    ],
    exports: [RouterModule]
})

export class AppRoutingModule {

}