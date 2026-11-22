import { TipoRecursoFisico } from '../../../models/enums/tipo-recurso-fisico.enum';
import { RecursoFisicoResponse } from '../../../models/physical-resource/recurso-fisico-response';
import { filtrarRecursos } from './filtrar-recursos';

const recursos: RecursoFisicoResponse[] = [
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
];

describe('filtrarRecursos', () => {
  it('devuelve todos los registros cuando no hay término', () => {
    expect(filtrarRecursos(recursos, '  ')).toHaveLength(2);
  });

  it('filtra por nombre, bloque o tipo sin distinguir mayúsculas', () => {
    expect(filtrarRecursos(recursos, 'redes').map((recurso) => recurso.id)).toEqual([1]);
    expect(filtrarRecursos(recursos, 'bloque b').map((recurso) => recurso.id)).toEqual([2]);
    expect(filtrarRecursos(recursos, 'laboratorio').map((recurso) => recurso.id)).toEqual([1]);
  });
});
