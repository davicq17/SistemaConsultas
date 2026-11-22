import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Rol } from '../../../../models/enums/rol.enum';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth.service';
import { CommonModule } from '@angular/common';
import { RegistroUsuarioRequest } from '../../../../models/auth/registro-usuario-request';

export interface ProgramaItem {
  id: number;
  nombre: string;
}

@Component({
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  selector: 'app-register.page',
  styleUrl: './register.page.css',
  templateUrl: './register.page.html',
})
export class RegisterPage {
  private fb = inject(FormBuilder); 
  private authservice = inject(AuthService); 
  private router = inject (Router);

  readonly esAdministrador = localStorage.getItem('rol') === Rol.ADMINISTRADOR;

  registerForm = this.fb.nonNullable.group({
      identificacion: ['',[Validators.required]],
      nombre: ['',[Validators.required]],
      apellido: ['',[Validators.required]],
      correo: ['',[Validators.required, Validators.email]],
      password: ['',[Validators.required, Validators.minLength(8)]],
      rol: [Rol.ESTUDIANTE, [Validators.required]],
      programaId: this.fb.control<number | null >(null, [Validators.required])
    });

    onSubmit(): void {
      if(this.registerForm.invalid){
        this.registerForm.markAllAsTouched();
        return;
      }

      const raw = this.registerForm.getRawValue();
      const payload: RegistroUsuarioRequest = {
        identificacion: raw.identificacion,
        nombre: raw.nombre,
        apellido: raw.apellido,
        correo: raw.correo,
        password: raw.password,
        rol: raw.rol,
        ...(raw.programaId != null ? { programaId: Number(raw.programaId) } : {})
      }

      this.authservice.register(payload)
      .subscribe({
        next: () => {
          this.router.navigate(['auth/login']);
        },
        error: (err) => {
          console.error('Error al registrar:', err); 
        }
      })
    }
  
}
