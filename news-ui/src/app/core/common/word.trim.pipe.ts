import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'wordTrim'
})
export class WordTrimPipe implements PipeTransform {
  transform(value: string, limit: number = 80): string {
    const words = value.split(' ');
    if (words.length > limit) {
      return words.slice(0, limit).join(' ') + '...';
    }
    return value;
  }

}
