import { BaseDTO } from "./base.dto";
import { RoleDTO } from "./role.dto";

export class UserDTO extends BaseDTO {
    username: string;
    password: string;
    fullName: string;
    roles: RoleDTO[];

    constructor(data:any) {
        super(data);
        this.username = data.username;
        this.password = data.password;
        this.roles = data.roles;
        this.fullName = data.fullName;
    }
}