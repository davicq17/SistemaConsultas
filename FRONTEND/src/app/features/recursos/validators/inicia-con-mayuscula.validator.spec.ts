import { FormControl } from '@angular/forms';
import { iniciaConMayuscula } from './inicia-con-mayuscula.validator';

describe('iniciaConMayuscula', () => {
  it('rechaza un nombre que empieza con minúscula', () => {
    const control = new FormControl('laboratorio de redes', iniciaConMayuscula());

    expect(control.hasError('iniciaMayuscula')).toBe(true);
  });

  it('acepta un nombre que empieza con mayúscula', () => {
    const control = new FormControl('Laboratorio de redes', iniciaConMayuscula());

    expect(control.valid).toBe(true);
  });

  it('deja el campo vacío al validador required', () => {
    const control = new FormControl('', iniciaConMayuscula());

    expect(control.hasError('iniciaMayuscula')).toBe(false);
  });
});
