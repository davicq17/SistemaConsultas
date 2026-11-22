import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { enviroment } from "../../../environments/enviroment";
import { Observable, tap } from "rxjs";

import { LoginRequest, } from "../../models/auth/login-request";
import { LoginResponse } from "../../models/auth/login-response";
import { RegistroUsuarioRequest } from "../../models/auth/registro-usuario-request";
import { ForgotPasswordRequest } from "../../models/auth/forgot-password-request";
import { ResetPasswordRequest } from "../../models/auth/reset-password-request";

@Injectable({providedIn: 'root'})
export class AuthService {
    private readonly API_URL= enviroment.apiUrl;
    private readonly TOKEN_KEY = 'access_token';

    constructor(private http: HttpClient , private router: Router){}

    login(request: LoginRequest): Observable<LoginResponse>{
        return this.http.post<LoginResponse>(`${this.API_URL}/auth/login`, request).pipe(
            tap((res) =>{
                localStorage.setItem(this.TOKEN_KEY, res.token);
                localStorage.setItem('rol', res.rol);
                localStorage.setItem('correo', res.correo);
                localStorage.setItem('nombre', res.nombre);
            })
        )
    }

    register(request: RegistroUsuarioRequest): Observable<any>{
        return this.http.post<RegistroUsuarioRequest>(`${this.API_URL}/auth/register`, request);
    }

    fotgotPassword(request: ForgotPasswordRequest): Observable<any>{
        return this.http.post<ForgotPasswordRequest>(`${this.API_URL}/auth/forgot-password`, request);
    }

    resetPassword(request : ResetPasswordRequest): Observable<any>{
        return this.http.post<ResetPasswordRequest>(`${this.API_URL}/auth/reset-password`,request);
    }

    logout(): void {
        localStorage.removeItem(this.TOKEN_KEY);
        localStorage.removeItem('rol');
        localStorage.removeItem('correo');
        localStorage.removeItem('nombre');
        void this.router.navigate(['auth/login']);
    }
}