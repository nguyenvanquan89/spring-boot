import { Component, OnInit} from '@angular/core';

import { Router, ActivatedRoute } from '@angular/router';

import { AuthService } from 'src/app/core/auth/auth.service';

@Component({
  selector: 'app-error-page',
  templateUrl: './error-page.component.html',
  styleUrls: ['./error-page.component.css']
})
export class ErrorPageComponent implements OnInit {
  errorMsg: string = '';

  constructor(
    private activedRoute: ActivatedRoute,
    private authService: AuthService,
  ) { 
    this.checkActivedRouteErrorPage();
  }

  ngOnInit(): void {
    this.authService.updateUserName();
  }
  //check actived screen home and then user click category of news
  checkActivedRouteErrorPage() {
    this.activedRoute.params.subscribe(
      params => {
        if (params['errorCode']) {
          let errorCode = params['errorCode'];
          switch (errorCode) {
            case '404':
              this.errorMsg = 'Page not found';
              break;
            case '403':
              this.errorMsg = 'Unauthorized, please login to access!';
              break;
            default:
              this.errorMsg = 'What is wrong !';
              break;
          }
        } else {
          this.errorMsg = 'What is wrong !';
        }
      }
    );
  }
}
