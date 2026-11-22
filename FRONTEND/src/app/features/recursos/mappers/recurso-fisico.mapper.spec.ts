import { TipoRecursoFisico } from '../../../models/enums/tipo-recurso-fisico.enum';
import { aRecursoFisicoRequest } from './recurso-fisico.mapper';

describe('aRecursoFisicoRequest', () => {
  it('convierte el modelo del formulario al modelo del servicio', () => {
    const request = aRecursoFisicoRequest({
      nombre: '  Sala de Tutorías  ',
      tipo: TipoRecursoFisico.LABORATORIO,
      bloqueId: 3,
    });

    expect(request).toEqual({
      nombre: 'Sala de Tutorías',
      tipo: TipoRecursoFisico.LABORATORIO,
      bloqueId: 3,
    });
  });
});
