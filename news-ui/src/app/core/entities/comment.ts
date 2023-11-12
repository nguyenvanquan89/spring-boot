import { Base } from "./base";
import { News } from "./news";
import { User } from "./user";

export interface Comment extends Base {
    content: string;
    user : User;
    news: News;
}