import { UserDTO } from "./user.dto";

export interface LoginResponse {
    jwtToken: string;
    user: UserDTO;
}