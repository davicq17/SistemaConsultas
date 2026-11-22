import { RecursoFisicoRequest } from '../../../models/physical-resource/registro-recurso-fisico-request';
import { RecursoFisicoFormValue } from '../models/recurso-fisico-form.model';

/** Convierte el modelo del formulario en el modelo que espera el servicio. */
export function aRecursoFisicoRequest(form: RecursoFisicoFormValue): RecursoFisicoRequest {
  return {
    nombre: form.nombre.trim(),
    tipo: form.tipo,
    bloqueId: Number(form.bloqueId),
  };
}
