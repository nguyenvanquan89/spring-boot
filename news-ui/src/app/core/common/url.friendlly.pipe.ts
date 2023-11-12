import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'urlFriendlly' })
export class UrlFriendllyPipe implements PipeTransform {
    transform(str: string) {
        str = str.normalize('NFD')
            .replace(/[\u0300-\u036f]/g, '')
            .replace(/đ/g, 'd')
            .replace(/Đ/g, 'D')
            .replace(/ /g, "-").toLowerCase();
        return str;
    }

}