import { TipoRecursoFisico } from '../../../models/enums/tipo-recurso-fisico.enum';

/**
 * Modelo del formulario de alta. Es distinto del modelo de datos:
 * aquí no hay id, bloqueNombre ni activo, porque los genera el servicio.
 */
export interface RecursoFisicoFormValue {
  nombre: string;
  tipo: TipoRecursoFisico;
  bloqueId: number;
}
