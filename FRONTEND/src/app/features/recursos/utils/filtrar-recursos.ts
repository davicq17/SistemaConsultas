import { RecursoFisicoResponse } from '../../../models/physical-resource/recurso-fisico-response';

export function filtrarRecursos(
  recursos: readonly RecursoFisicoResponse[],
  termino: string,
): RecursoFisicoResponse[] {
  const texto = termino.trim().toLocaleLowerCase('es');
  if (texto.length === 0) {
    return [...recursos];
  }

  return recursos.filter((recurso) =>
    [recurso.nombre, recurso.bloqueNombre, recurso.tipo].some((campo) =>
      campo.toLocaleLowerCase('es').includes(texto),
    ),
  );
}
