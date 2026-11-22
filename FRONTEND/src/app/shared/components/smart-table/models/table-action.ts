export interface TableAction<T> {
    label: string;
    className?: string;
    action: (row: T) => void;
}
