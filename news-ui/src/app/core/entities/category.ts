import { Base } from "./base";

export interface Category extends Base {
    name: string;
    code: string;
    totalNews: number;
}