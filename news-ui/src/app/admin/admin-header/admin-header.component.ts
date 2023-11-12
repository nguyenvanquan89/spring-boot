import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CommonConstants } from 'src/app/core/common/common.constants';

@Component({
  selector: 'app-admin-header',
  templateUrl: './admin-header.component.html',
  styleUrls: ['./admin-header.component.css']
})
export class AdminHeaderComponent implements OnInit, OnDestroy {
  activeNavItem: number = 0;
  userNameLoggedIn: string = '';
  private subscriptionName: Subscription;
  hasRoleAdmin: boolean;
  hasRoleAuthor: boolean;
  hasRoleEditor: boolean;


  constructor(
    private authService: AuthService,
    private router: Router,
  ) {
    this.subscriptionName = this.getUserNameLoggedIn();
    this.hasRoleAdmin = this.authService.isPermit([CommonConstants.ROLE_ADMIN]);
    this.hasRoleAuthor = this.authService.isPermit([CommonConstants.ROLE_AUTHOR]);
    this.hasRoleEditor = this.authService.isPermit([CommonConstants.ROLE_EDITOR]);
  }

  ngOnInit(): void {

  }

  ngOnDestroy(): void {
    this.subscriptionName.unsubscribe();
  }

  //get user logged in by subject
  getUserNameLoggedIn() {
    return this.authService.getUserName().subscribe({
      next: (data) => {
        this.userNameLoggedIn = data['username'];
        if (!this.authService.isLoggedIn()) {
          this.router.navigate(['/login']);
        }
      }
    });
  }

  //set active item of menu
  setActiveNavItem(index: number) {
    this.activeNavItem = index;
  }

  //logout and then redirect to home page
  logout() {
    this.authService.removeUserLoggedIn();
    this.userNameLoggedIn = '';
    this.router.navigate(['/']);
  }
}
