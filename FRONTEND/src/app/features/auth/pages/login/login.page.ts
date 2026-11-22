import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../../core/services/auth.service';
import { Rol } from '../../../../models/enums/rol.enum';

@Component({
  selector: 'app-login.page',
  standalone: true,
  imports: [ReactiveFormsModule, ],
  styleUrl: './login.page.css',
  templateUrl: './login.page.html',
})
export class LoginPage {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router ){
    this.loginForm = this.fb.nonNullable.group({
      correo: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    })
  }

  private redirigir(rol: Rol){
    if(rol === 'ADMINISTRADOR'){
      this.router.navigate(['users/list']);
    }
    if(rol === 'ESTUDIANTE'){
      this.router.navigate(['']);
    }else if( rol === 'DOCENTE'){
      this.router.navigate(['']);
    }
  };

  onSumit(): void{
    if(this.loginForm.invalid){
      this.loginForm.markAllAsTouched();
      return;
    }

    const raw = this.loginForm.getRawValue();

    this.authService.login(raw)
    .subscribe({
      next: (res) => {
        this.redirigir(res.rol);
      },
      error: (err) => {
        console.log("Error en el login:",err);
      }
    })
  }
}
