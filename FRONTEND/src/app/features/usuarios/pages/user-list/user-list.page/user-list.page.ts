import { Component, inject } from '@angular/core';
import { UsuarioService } from '../../../services/usuario.service';
import { Router } from '@angular/router';

import { SmartTableComponent } from '../../../../../shared/components/smart-table/smart-table.component/smart-table.component';
import { UsuarioResponse } from '../../../../../models/user/usuario-response';
import { toSignal } from '@angular/core/rxjs-interop';
import { catchError, of } from 'rxjs';
import { TableConfig } from '../../../../../shared/components/smart-table/models/table-config';

@Component({
  imports: [SmartTableComponent],
  standalone: true,
  selector: 'app-user-list.page',
  styleUrl: './user-list.page.css',
  templateUrl: './user-list.page.html',
})
export class UserListPage {
  private userService = inject(UsuarioService);
  private router = inject(Router);


  users = toSignal(
    this.userService.userList().pipe(
      catchError(error => {
        console.error('Error al cargar los usuarios:', error);
        return of([]);
      })
    ),
    { initialValue: [] as UsuarioResponse[]}
  );

  tableConfig: TableConfig<UsuarioResponse> = {
    columns: [
      {field: 'identificacion',header: 'Identificación'},
      {field: 'nombre',header: 'Nombre'},
      {field: 'correo',header: 'Correo'},
      {field: 'rol',header: 'Rol'},
      {field: 'activo',header: 'Estado',
        type: 'tag',
        tagConfig: {
          valueFn: user => user.activo ? 'Activo' : 'Inactivo',
          classFn: user => user.activo ? 'tag-active' : 'tag-inactive'
        }
      }
    ],

    actions: [
      {label: 'Ver', action: user => this.verUsuario(user)},
      {label: 'Editar', action: user => this.editarUsuario(user)}
    ],

    globalFilter: true,
    paginator: true,
    rows: 10,
    rowsPerPageOptions: [10,20,40]
  };

  verUsuario(user: UsuarioResponse): void {
    console.log('Usuario seleccionado:', user);
  }

  editarUsuario(user: UsuarioResponse): void {
    console.log('Editar usuario:', user);
  }
}
