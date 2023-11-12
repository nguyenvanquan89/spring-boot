import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { CategoryDTO } from 'src/app/core/dtos/category.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { CategoryService } from 'src/app/core/service/category.service';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CommonConstants } from 'src/app/core/common/common.constants';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit, OnDestroy {

  lstResult: CategoryDTO[] = [];
  userNameLoggedIn: string = '';
  activeNavItem: number = 0;
  private subscriptionName: Subscription;

  constructor(
    private categoryService: CategoryService,
    private authService: AuthService,
    private router: Router,
    private activedRoute: ActivatedRoute,
  ) {
    this.subscriptionName  = this.getUserNameLoggedIn();
  }

  ngOnInit(): void {
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
    //this.userNameLoggedIn = this.authService.getUserNameFromToken();
    
    this.headerActivedRoute();
    this.loadCategory(searchDto);
  }

  ngOnDestroy() { // It's a good practice to unsubscribe to ensure no memory leaks
    this.subscriptionName.unsubscribe();

  }

  //Load category
  loadCategory(searchDto: SearchDTO[]) {
    this.categoryService.findAllCategoriesByPaging(searchDto).subscribe({
      next: (data) => {
        this.lstResult = data.lstResult;
      },
      error: (error) => {
        console.log(error);
      },
      complete: () => {
        console.log("Load category success");
      }
    });
  }

  //logout and then redirect to home page
  logout() {
    this.authService.removeUserLoggedIn();
    this.userNameLoggedIn = '';
    this.router.navigate(['/']);
  }

  //get user logged in by subject
  getUserNameLoggedIn() {
    return this.authService.getUserName().subscribe({
      next: (data) => {
        this.userNameLoggedIn = data['username'];
        
      }
    });
  }

  //check header actived route
  headerActivedRoute() {
    this.activedRoute.params.subscribe(
      params => {
        if (params['categoryId']) {
          this.activeNavItem = params['categoryId'] ?? 0;
        }
      }
    );
  }

}
