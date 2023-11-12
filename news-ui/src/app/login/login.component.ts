import { Component, OnInit } from '@angular/core';

import { Router, ActivatedRoute } from '@angular/router';
import { UserService } from '../core/service/user.service';
import { RegisterDTO } from '../core/dtos/register.dto';
import { LoginDTO } from '../core/dtos/login.dto';
import { LoginResponse } from '../core/dtos/login.response';
import { AuthService } from '../core/auth/auth.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms'
import { CommonConstants } from '../core/common/common.constants';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  //This object to manage data form login
  loginForm: FormGroup;

  //Register form data
  formRegister: any = {
    fullName: null,
    username: null,
    password: null,
    retypePassword: null,
    status: 1,
    isAccept: false,
    isNotMatchPassword: false
  };

  errorMsgLogin = '';
  isLoginTab = true;
  errorMsgRegister = '';
  successMsgRegister = '';

  constructor(
    private router: Router,
    private userService: UserService,
    private authService: AuthService,
    private fb: FormBuilder,
    private activedRoute: ActivatedRoute,
  ) {
    //init form login
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      isRemember: [false]
    });
  }

  ngOnInit(): void {
      if(this.authService.isLoggedIn()) {
        this.router.navigate(['/']);
      }
    //init login or register tab
    this.checkActivedLoginOrRegister();
  }

    //check actived screen login or register
    checkActivedLoginOrRegister() {
      this.activedRoute.queryParams.subscribe(
        params => {
          this.isLoginTab = params['register'] ? false : true;
        }
      );
    }

  //Login method
  login() {
    let loginDto: LoginDTO = {
      'username': '',
      'password': ''
    };
    //Copy data form 'formLogin' to 'loginDto' by spread(...) operators
    loginDto = {
      ...loginDto,
      ...this.loginForm.value
    };
    this.userService.loginUser(loginDto).subscribe({
      next: (data: LoginResponse) => {
        this.errorMsgLogin = '';
        this.errorMsgRegister ='';
        this.successMsgRegister ='';
        //save user logged in to local
        this.authService.saveUserLoggedIn(data);
        let expectedRoles : string[] = [CommonConstants.ROLE_ADMIN, 
                                        CommonConstants.ROLE_EDITOR, 
                                        CommonConstants.ROLE_AUTHOR];
        let isAdmin : boolean = this.authService.isPermit(expectedRoles);
        if(isAdmin) { // redirect to admin page
          this.router.navigate(['/admin']);
        } else {
          //Redirect to home screen if user has role user
          this.router.navigate(['/']);
        }
      },
      error: err => {
        //delete user logged in
        this.authService.removeUserLoggedIn();
        this.errorMsgLogin = err.error.message;
        this.errorMsgRegister ='';
        this.successMsgRegister ='';
      },
      complete: () => {
        console.log("Login success!");
      }
    });
  }

  //Register user
  registerUser() {
    const registerDto: RegisterDTO = {
      'fullName': this.formRegister.fullName,
      'username': this.formRegister.username,
      'password': this.formRegister.password,
      'status': this.formRegister.status
    };
    this.userService.customerRegisterUserAccount(registerDto).subscribe({
      next: data => {
        this.errorMsgLogin = '';
        this.errorMsgRegister ='';
        this.successMsgRegister = data.message;
        //Register success then active to login screen tab
        this.isLoginTab = true;
      },
      error: err => {
        this.errorMsgRegister = err.error.message;
        this.errorMsgLogin = '';
        this.successMsgRegister ='';
      },
      complete: () => {
        console.log("Register success!");
      }
    });
  }

  //check password and retype password is match of register screen
  checkPasswordMatch() {
    if (this.formRegister.password !== this.formRegister.retypePassword) {
      this.formRegister.isNotMatchPassword = true;
    } else {
      this.formRegister.isNotMatchPassword = false;
    }
  }

}