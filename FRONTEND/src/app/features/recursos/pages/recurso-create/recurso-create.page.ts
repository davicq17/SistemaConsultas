import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { RecursoFisicoResponse } from '../../../../models/physical-resource/recurso-fisico-response';
import { TipoRecursoFisico } from '../../../../models/enums/tipo-recurso-fisico.enum';
import { BLOQUES } from '../../data/bloques';
import { aRecursoFisicoRequest } from '../../mappers/recurso-fisico.mapper';
import { RecursoFisicoFormValue } from '../../models/recurso-fisico-form.model';
import { RecursosService } from '../../services/recursos.service';
import { iniciaConMayuscula } from '../../validators/inicia-con-mayuscula.validator';

@Component({
  selector: 'app-recurso-create',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  styleUrl: './recurso-create.page.css',
  templateUrl: './recurso-create.page.html',
})
export class RecursoCreatePage {
  private readonly fb = inject(FormBuilder);
  private readonly recursosService = inject(RecursosService);

  readonly bloques = BLOQUES;
  readonly tipos = Object.values(TipoRecursoFisico);

  /** FormBuilder tipado: un control por cada campo del modelo de formulario. */
  readonly form = this.fb.nonNullable.group({
    nombre: [
      '',
      [
        Validators.required,
        Validators.minLength(4),
        Validators.maxLength(50),
        iniciaConMayuscula(),
      ],
    ],
    tipo: [TipoRecursoFisico.SALON, [Validators.required]],
    bloqueId: [1, [Validators.required, Validators.min(1)]],
  });

  readonly nombre = this.form.controls.nombre;
  readonly tipo = this.form.controls.tipo;
  readonly bloqueId = this.form.controls.bloqueId;

  private readonly creadoInterno = signal<RecursoFisicoResponse | null>(null);
  readonly creado = this.creadoInterno.asReadonly();

  guardar(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const valores: RecursoFisicoFormValue = this.form.getRawValue();
    this.creadoInterno.set(this.recursosService.crear(aRecursoFisicoRequest(valores)));

    // El listado se alimenta de la signal del servicio: ya cambió, sin recargar.
    this.form.reset({ nombre: '', tipo: TipoRecursoFisico.SALON, bloqueId: 1 });
  }
}
