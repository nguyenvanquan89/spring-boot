import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/core/auth/auth.service';
import { RoleDTO } from 'src/app/core/dtos/role.dto';
import { SearchDTO } from 'src/app/core/dtos/search.dto';
import { Role } from 'src/app/core/entities/role';
import { RoleService } from 'src/app/core/service/role.service';

@Component({
  selector: 'app-admin-role-edit',
  templateUrl: './admin-role-edit.component.html',
  styleUrls: ['./admin-role-edit.component.css']
})
export class AdminRoleEditComponent implements OnInit {
  roleId: string = '';
  roleDto: RoleDTO;
  roleForm: FormGroup;
  msgSuccess: string = '';
  msgErrorServer: string = '';

  constructor(
    private roleService: RoleService,
    private activedRoute: ActivatedRoute,
    private authService: AuthService,
    private fb: FormBuilder,
  ) {
    this.roleDto = this.initDto();
    this.roleForm = this.initForm();
  }

  ngOnInit(): void {
    this.activedRoute.params.subscribe(
      params => {
        console.log("role id = " + params['roleId']);
        if (params['roleId']) {
          this.roleId = params['roleId'];
        }
      }
    );
    this.getRoleById();
  }


  //init Role DTO
  private initDto() {
    let data = {
      'id': '',
      'name': '',
      'code': '',
    };
    return new RoleDTO(data);
  }

  //Init role form
  private initForm(initData?: any) {
    return this.fb.group({
      id: [initData?.id],
      name: [initData?.name, [Validators.required, Validators.minLength(5)]],
      code: [initData?.code, [Validators.required, Validators.minLength(4)]],
    });
  }

  //load category by id
  private getRoleById() {
    //if insert news 
    if (this.roleId == '') {
      return;
    }
    //create search DTO
    const searchDto: SearchDTO[] = [
      {
        'key': 'id',
        'value': this.roleId
      }
    ];
    //call service to find category by id
    this.roleService.findOneRoleById(searchDto).subscribe({
      next: (data: Role) => {
        this.roleDto = { ...data };
        this.roleForm = this.initForm(this.roleDto);
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
    //use spread() operator to copy properties from roleForm to roleDto 
    this.roleDto = {
      ...this.roleDto,
      ...this.roleForm.value
    }
    //if have id then update or insert
    this.roleService.saveRoleData(this.roleDto).subscribe({
      next: (res) => {
        this.roleDto = { ...res.data };
        this.roleForm = this.initForm(this.roleDto);
        this.msgSuccess = res.message;
        this.msgErrorServer = '';
      },
      error: (error) => {
        this.msgErrorServer = error.error.message;
        this.msgSuccess = '';
      },
    });
  }

}
