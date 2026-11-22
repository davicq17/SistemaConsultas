import { Component, computed, inject, input, numberAttribute } from '@angular/core';
import { RouterLink } from '@angular/router';
import { RecursosService } from '../../services/recursos.service';

@Component({
  selector: 'app-recurso-detail',
  standalone: true,
  imports: [RouterLink],
  styleUrl: './recurso-detail.page.css',
  templateUrl: './recurso-detail.page.html',
})
export class RecursoDetailPage {
  /** El parámetro de la ruta llega como signal mediante withComponentInputBinding(). */
  readonly id = input.required({ transform: numberAttribute });

  private readonly recursosService = inject(RecursosService);

  readonly recurso = computed(() =>
    this.recursosService.recursos().find((recurso) => recurso.id === this.id()) ?? null,
  );
}
