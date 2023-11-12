export class BaseDTO {
    id: number;
    createdDate?: Date;
    createdBy? : string;
    modifiedDate?: Date;
    modifiedBy?: string;
    isSelected?: boolean; //use for delete item

    constructor(data: any) {
        this.id = data.id;
    }
}