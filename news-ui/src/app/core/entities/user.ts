import { Base } from "./base";
import { Role } from "./role";

export interface User extends Base {
    username: string;
    password: string;
    fullName: string;
    status: number;
    roles: Role[];
}