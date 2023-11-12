export class SearchDTO {
    key: string;
    value: any;

    constructor(data: any) {
        this.key = data.key;
        this.value = data.value;
    }
}