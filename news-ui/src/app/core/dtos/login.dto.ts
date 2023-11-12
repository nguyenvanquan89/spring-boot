import {
    IsNotEmpty,
    IsString,
    IsDate,
    IsPhoneNumber,
    IsEmail
} from 'class-validator'

export class LoginDTO {
    @IsEmail()
    @IsNotEmpty()
    username: string;

    @IsNotEmpty()
    password: string;

    constructor(data: any) {
        this.username = data.username;
        this.password = data.password;
    }
}