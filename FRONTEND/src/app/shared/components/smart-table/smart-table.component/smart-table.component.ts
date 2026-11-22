import { Component, input, computed } from '@angular/core';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { toSignal } from '@angular/core/rxjs-interop';
import { debounceTime, distinctUntilChanged, startWith } from 'rxjs';
import { TableConfig } from '../models/table-config';

@Component({
  imports: [ReactiveFormsModule],
  standalone:true,
  selector: 'app-smart-table',
  styleUrl: './smart-table.component.css',
  templateUrl: './smart-table.component.html',
})
export class SmartTableComponent<T> {
  data = input.required<T[]>();
  config = input.required<TableConfig<T>>();

  searchControl = new FormControl('', {
    nonNullable: true
  });

  searchTerm = toSignal(
    this.searchControl.valueChanges.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      startWith('')
    ),
    { initialValue: '' }
  );

  filteredData = computed(() => {
    const data = this.data();
    const term = this.searchTerm().trim().toLowerCase();

    if(!term){
      return data;
    }

    const searchableColumns = this.config().columns.filter(
      col => col.searchable !== false
    );

    return data.filter( row => 
      searchableColumns.some(col => {
        const value = this.resolveFieldData(row, col.field);

        return String(value ?? '').toLowerCase().includes(term);
      })
    );
  });

  resolveFieldData(row: T, field: string): unknown {
    return field.split('.').reduce<unknown>((value, key) => {

      if(value == null || typeof value !== 'object'){
        return null;
      }

      return (value as Record<string, unknown>)[key];
    }, row);
  }

  clearFilter(): void {
    this.searchControl.setValue('');
  }
}
