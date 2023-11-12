import { BaseDTO } from "./base.dto";
import { CategoryDTO } from "./category.dto";

export class NewsDTO extends BaseDTO {

    title: string;
    thumbnail : string;
    shortDescription : string;
    content : string;
    category : CategoryDTO;
    //comments
    constructor (data: any) {
        super(data);
        this.title = data.title;
        this.thumbnail = data.thumbnail;
        this.shortDescription = data.shortDescription;
        this.content = data.content;
        this.category = data.category;
    }
}