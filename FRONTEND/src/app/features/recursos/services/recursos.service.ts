import { Injectable, signal } from '@angular/core';
import { RecursoFisicoResponse } from '../../../models/physical-resource/recurso-fisico-response';
import { RecursoFisicoRequest } from '../../../models/physical-resource/registro-recurso-fisico-request';
import { TipoRecursoFisico } from '../../../models/enums/tipo-recurso-fisico.enum';
import { nombreBloque } from '../data/bloques';

@Injectable({ providedIn: 'root' })
export class RecursosService {
  private readonly estado = signal<RecursoFisicoResponse[]>([
    {
      id: 1,
      nombre: 'Laboratorio de Redes',
      bloqueId: 1,
      bloqueNombre: 'Bloque A',
      tipo: TipoRecursoFisico.LABORATORIO,
      activo: true,
    },
    {
      id: 2,
      nombre: 'Auditorio Central',
      bloqueId: 2,
      bloqueNombre: 'Bloque B',
      tipo: TipoRecursoFisico.AUDITORIO,
      activo: true,
    },
    {
      id: 3,
      nombre: 'Salón de Sistemas',
      bloqueId: 1,
      bloqueNombre: 'Bloque A',
      tipo: TipoRecursoFisico.SALON,
      activo: true,
    },
    {
      id: 4,
      nombre: 'Sala de Consultas',
      bloqueId: 3,
      bloqueNombre: 'Bloque C',
      tipo: TipoRecursoFisico.SALON,
      activo: true,
    },
  ]);

  /** Única forma de leer el listado: los componentes no pueden modificarlo. */
  readonly recursos = this.estado.asReadonly();

  crear(request: RecursoFisicoRequest): RecursoFisicoResponse {
    const id = this.estado().reduce((maximo, recurso) => Math.max(maximo, recurso.id), 0) + 1;
    const recurso: RecursoFisicoResponse = {
      id,
      nombre: request.nombre.trim(),
      bloqueId: request.bloqueId,
      bloqueNombre: nombreBloque(request.bloqueId),
      tipo: request.tipo,
      activo: true,
    };

    this.estado.update((lista) => [...lista, recurso]);
    return recurso;
  }
}
