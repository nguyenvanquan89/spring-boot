import { BaseDTO } from "./base.dto";
import { NewsDTO } from "./news.dto";
import { UserDTO } from "./user.dto";

export class CommentDTO extends BaseDTO {
    content: string;
    user : UserDTO;
    news: NewsDTO;

    constructor (data:any) {
        super(data);
        this.content = data.content;
        this.user = data.user;
        this.news = data.news;
    }
}