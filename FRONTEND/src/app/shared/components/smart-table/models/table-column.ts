export interface TableColumn<T> {
    field: keyof T & string;
    header: string;
    sortable?: boolean;
    searchable?: boolean;
    wrap?: boolean;
    type?: 'text' | 'tag';
    formatFn?: (value: unknown, row: T) => string;
    tagConfig?: {
        valueFn: (row: T) => string;
        classFn?: (row: T) => string;
    };
}
