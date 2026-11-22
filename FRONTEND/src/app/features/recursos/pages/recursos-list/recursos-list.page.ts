import { Component, computed, inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { debounceTime, distinctUntilChanged, startWith } from 'rxjs';
import { RecursosService } from '../../services/recursos.service';
import { filtrarRecursos } from '../../utils/filtrar-recursos';

@Component({
  selector: 'app-recursos-list',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  styleUrl: './recursos-list.page.css',
  templateUrl: './recursos-list.page.html',
})
export class RecursosListPage {
  private readonly recursosService = inject(RecursosService);

  readonly busqueda = new FormControl('', { nonNullable: true });

  /** toSignal cancela la suscripción al destruir el componente: no queda ninguna abierta. */
  readonly termino = toSignal(
    this.busqueda.valueChanges.pipe(debounceTime(300), distinctUntilChanged(), startWith('')),
    { initialValue: '' },
  );

  readonly recursos = computed(() =>
    filtrarRecursos(this.recursosService.recursos(), this.termino()),
  );
}
