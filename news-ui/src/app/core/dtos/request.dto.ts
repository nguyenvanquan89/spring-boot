import { CommonConstants } from "../common/common.constants";

export class RequestDTO {
    subUrl: string;
    dataRequest: any;
    language: string;
    nonContentType?: boolean = false;

    constructor(data: any) {
        this.subUrl = data.subUrl;
        this.dataRequest = data.dataRequest;
        this.language = data.language;
        this.nonContentType = data.nonContentType;
    }
}