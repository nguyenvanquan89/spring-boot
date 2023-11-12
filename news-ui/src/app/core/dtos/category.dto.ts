import { BaseDTO } from "./base.dto";

export class CategoryDTO extends BaseDTO {
    name: string;
    code: string;
    totalNews: number;
    
    constructor(data: any) {
        super(data);
        this.name = data.name;
        this.code = data.code;
        this.totalNews = data.totalNews;
    }

}