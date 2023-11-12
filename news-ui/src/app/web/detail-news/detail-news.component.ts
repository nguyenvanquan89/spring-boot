import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { Environment } from 'src/app/core/common/evironment.constants';
import { UrlConstants } from 'src/app/core/common/url.constants';
import { NewsDTO } from 'src/app/core/dtos/news.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { NewsService } from 'src/app/core/service/news.service';

@Component({
  selector: 'app-detail-news',
  templateUrl: './detail-news.component.html',
  styleUrls: ['./detail-news.component.css']
})
export class DetailNewsComponent implements OnInit {

  newsId: string = '';
  newsDto?: NewsDTO;

  constructor(
    private newsService: NewsService,
    private activedRoute: ActivatedRoute,
    private authService: AuthService,
  ) {
  }

  ngOnInit(): void {
    this.activedRoute.params.subscribe(
      params => { this.newsId = params['newsId']; }
    );
    this.getNewsById();
  }

  getNewsById() {
    const searchDto: SearchDTO[] = [
      {
        'key': 'id',
        'value': this.newsId
      }
    ];
    this.newsService.findOneNewsById(searchDto).subscribe({
      next: (data: NewsDTO) => {
        let thumbnail = data.thumbnail;
        if (!thumbnail) {
          thumbnail = 'dummy-400X400.webp';
        }
        data.thumbnail = Environment.apiBaseUrl + UrlConstants.NEWS_IMAGE_URL + thumbnail;
        this.newsDto = data;
      },
      error: (error) => {
        console.log(error.error.message);
      },
      complete: () => {
        console.log("Loaded news id=" + this.newsId);
        this.authService.updateUserName();
      },
    });
  }

}
