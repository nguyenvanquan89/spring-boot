import {
    IsNotEmpty,
    IsString,
    IsDate,
    IsPhoneNumber,
    IsEmail
} from 'class-validator'

export class RegisterDTO {
    @IsNotEmpty()
    fullName: string;

    @IsEmail()
    @IsNotEmpty()
    username: string;

    @IsNotEmpty()
    password: string;
    status: number = 1;

    constructor(data: any) {
        this.fullName = data.fullName;
        this.username = data.username;
        this.password = data.password;
        this.status = data.status || 1;
    }
}