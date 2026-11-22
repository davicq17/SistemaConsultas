import { TableColumn } from "./table-column";
import { TableAction } from "./table-action";

export interface TableConfig<T> {
    columns: TableColumn<T>[];
    actions?: TableAction<T>[];
    paginator?: boolean;
    rows?: number;
    rowsPerPageOptions?: number[];
    globalFilter?: boolean;
}
