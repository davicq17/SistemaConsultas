import { TestBed } from '@angular/core/testing';
import { TipoRecursoFisico } from '../../../models/enums/tipo-recurso-fisico.enum';
import { RecursosService } from './recursos.service';

describe('RecursosService', () => {
  it('agrega el registro al listado sin recargar y le asigna el siguiente id', () => {
    TestBed.configureTestingModule({});
    const service = TestBed.inject(RecursosService);
    const cantidadInicial = service.recursos().length;

    const creado = service.crear({
      nombre: ' Aula de Innovación ',
      tipo: TipoRecursoFisico.SALON,
      bloqueId: 2,
    });

    expect(creado.id).toBe(cantidadInicial + 1);
    expect(creado.nombre).toBe('Aula de Innovación');
    expect(creado.bloqueNombre).toBe('Bloque B');
    expect(service.recursos()).toHaveLength(cantidadInicial + 1);
    expect(service.recursos().at(-1)).toEqual(creado);
  });
});
