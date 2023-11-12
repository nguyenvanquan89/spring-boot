import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { AdminHomeComponent } from './admin-home/admin-home.component';
import { AdminNewsComponent } from './admin-news/admin-news.component';
import { AdminNewsEditComponent } from './admin-news/admin-news-edit.component';
import { AdminCategoryComponent } from './admin-category/admin-category.component';
import { AdminCommentComponent } from './admin-comment/admin-comment.component';
import { AdminRolesComponent } from './admin-roles/admin-roles.component';
import { AdminUserComponent } from './admin-user/admin-user.component';
import { authGuard } from '../core/auth/auth.guard';
import { CommonConstants } from '../core/common/common.constants';
import { AdminCategoryEditComponent } from './admin-category/admin-category-edit.component';
import { AdminCommentEditComponent } from './admin-comment/admin-comment-edit.component';
import { AdminRoleEditComponent } from './admin-roles/admin-role-edit.component';
import { AdminUserEditComponent } from './admin-user/admin-user-edit.component';


const routes: Routes = [
    {
        path: 'admin',
        component: AdminComponent,
        children: [
            {
                path: ':view',
                component: AdminHomeComponent,
            },
            {
                path: 'news/edit/:title/:newsId/:view',
                component: AdminNewsEditComponent,
            },
            {
                path: 'news/add/:view',
                component: AdminNewsEditComponent,
            },
            {
                path: 'news/:view',
                component: AdminNewsComponent,
            },
            {
                path: 'comments/edit/:title/:commentId/:view',
                component: AdminCommentEditComponent,
            },
            {
                path: 'comments/add/:view',
                component: AdminCommentEditComponent,
            },
            {
                path: 'comments/:view',
                component: AdminCommentComponent,
            },
            {
                path: 'categories/edit/:title/:categoryId/:view',
                component: AdminCategoryEditComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN, CommonConstants.ROLE_EDITOR]
                },
            },
            {
                path: 'categories/add/:view',
                component: AdminCategoryEditComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN, CommonConstants.ROLE_EDITOR]
                },
            },
            {
                path: 'categories/:view',
                component: AdminCategoryComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN, CommonConstants.ROLE_EDITOR]
                },
            },
            {
                path: 'roles/edit/:name/:roleId/:view',
                component: AdminRoleEditComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN]
                },
            },
            {
                path: 'roles/add/:view',
                component: AdminRoleEditComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN]
                },
            },
            {
                path: 'roles/:view',
                component: AdminRolesComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN]
                },
            },
            {
                path: 'users/edit/:username/:userId/:view',
                component: AdminUserEditComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN]
                },
            },
            {
                path: 'users/add/:view',
                component: AdminUserEditComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN]
                },
            },
            {
                path: 'users/:view',
                component: AdminUserComponent,
                canActivate: [authGuard],
                data: {
                    expectedRole: [CommonConstants.ROLE_ADMIN]
                },
            },
        ],
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(routes),
    ],
    exports: [RouterModule]
})

export class AdminRoutingModule {

}