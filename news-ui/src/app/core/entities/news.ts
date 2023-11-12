import { Base } from "./base";
import { Category } from "./category";

export interface News extends Base {
    title: string;
    thumbnail : string;
    shortDescription : string;
    content : string;
    category : Category;
}