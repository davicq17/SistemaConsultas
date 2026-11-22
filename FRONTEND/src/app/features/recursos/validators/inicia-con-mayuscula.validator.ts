import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

/** Validador propio: fuerza a que el nombre legible empiece con letra mayúscula. */
export function iniciaConMayuscula(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const valor = control.value;
    if (typeof valor !== 'string' || valor.trim().length === 0) {
      return null; // El validador required ya reporta el campo vacío.
    }
    return /^\p{Lu}/u.test(valor.trim()) ? null : { iniciaMayuscula: true };
  };
}
