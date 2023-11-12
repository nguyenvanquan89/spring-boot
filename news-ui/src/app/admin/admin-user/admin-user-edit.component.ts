import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { CommonConstants } from 'src/app/core/common/common.constants';
import { RoleDTO } from 'src/app/core/dtos/role.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { UserDTO } from 'src/app/core/dtos/user.dto';
import { User } from 'src/app/core/entities/user';
import { RoleService } from 'src/app/core/service/role.service';
import { UserService } from 'src/app/core/service/user.service';

@Component({
  selector: 'app-admin-user-edit',
  templateUrl: './admin-user-edit.component.html',
  styleUrls: ['./admin-user-edit.component.css']
})
export class AdminUserEditComponent implements OnInit {
  userId: string = '';
  userDto: UserDTO;
  userForm: FormGroup;
  msgSuccess: string = '';
  msgErrorServer: string = '';
  isNotMatchPassword: boolean = false;
  roles: RoleDTO[] = [];

  constructor(
    private userService: UserService,
    private roleService: RoleService,
    private activedRoute: ActivatedRoute,
    private authService: AuthService,
    private fb: FormBuilder,
  ) {
    this.userDto = this.initDto();
    this.userForm = this.initForm();
  }
  ngOnInit(): void {
    this.activedRoute.params.subscribe(
      params => {
        console.log("User id = " + params['userId']);
        if (params['userId']) {
          this.userId = params['userId'];
        }
      });
    this.getUserById();
    this.getRoles();
  }
  //init Comment DTO
  private initDto() {
    let data = {
      'id': '',
      'username': '',
      'password': '',
      'fullName': '',
      'roles': [{
        'id': '',
      }],
    };
    return new UserDTO(data);
  }

  //Init User form
  private initForm(initData?: any) {
    return this.fb.group({
      id: [initData?.id],
      username: [{ 'value': initData?.username, disabled: initData?.id > 0 }, [Validators.email, Validators.minLength(6)]],
      password: [initData?.password, [Validators.required, Validators.minLength(6)]],
      retypePassword: [initData?.password, [Validators.required, Validators.minLength(6)]],
      fullName: [initData?.fullName, [Validators.required, Validators.minLength(10)]],
      roleId: [initData?.roles[0]?.id, [Validators.required]],
    });
  }

  //load Role
  private getRoles() {
    let searchDto: SearchDTO[] = [
      {
        'key': 'currentPage',
        'value': CommonConstants.CURRENT_PAGE,
      },
      {
        'key': 'itemPerPage',
        'value': CommonConstants.ITEM_PERPAGE
      },
    ];
    this.roleService.findAllRolesByPaging(searchDto).subscribe({
      next: (data) => {
        this.roles = data.lstResult;
      },
      error: (error) => {
        this.msgErrorServer = error.error.message;
      },
    });
  }

  //load User by id
  private getUserById() {
    //if insert 
    if (this.userId == '') {
      return;
    }
    //create search DTO
    const searchDto: SearchDTO[] = [
      {
        'key': 'id',
        'value': this.userId
      }
    ];
    //call service to find by id
    this.userService.findOneUserById(searchDto).subscribe({
      next: (data: User) => {
        this.userDto = { ...data };
        this.userForm = this.initForm(this.userDto);
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
    //Exclude roleId properties
    const { roleId, ...tmpForm } = this.userForm.value;
    //use spread() operator to copy properties from tempForm to userDto 
    this.userDto = {
      ...this.userDto,
      ...tmpForm
    }
    //set role id new
    if (this.userDto.roles.length == 0) {
      this.userDto.roles.push(new RoleDTO({ 'id': this.userForm.value['roleId'] }));
    } else {
      this.userDto.roles[0].id = this.userForm.value['roleId'];
    }

    //if have id then update or insert
    this.userService.saveUserData(this.userDto).subscribe({
      next: (res) => {
        this.userDto = { ...res.data };
        this.userForm = this.initForm(this.userDto);
        this.msgSuccess = res.message;
        this.msgErrorServer = '';
      },
      error: (error) => {
        this.msgErrorServer = error.error.message;
        this.msgSuccess = '';
      },
    });
  }

  //check password and retype password is match of register screen
  checkPasswordMatch() {
    if (this.userForm.controls['password'].value !== this.userForm.controls['retypePassword'].value) {
      this.isNotMatchPassword = true;
    } else {
      this.isNotMatchPassword = false;
    }
  }

}
