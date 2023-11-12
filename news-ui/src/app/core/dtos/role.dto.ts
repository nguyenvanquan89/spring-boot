import { BaseDTO } from "./base.dto";

export class RoleDTO extends BaseDTO {
    code: string;
    name: string;

    constructor(data:any) {
        super(data);
        this.code = data.code;
        this.name = data.name;
    }
}