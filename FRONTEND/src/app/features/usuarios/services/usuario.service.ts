import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { enviroment } from '../../../../environments/enviroment';
import { Observable} from 'rxjs';

import { UsuarioResponse } from '../../../models/user/usuario-response';
import { UpdateUsuarioRequest } from '../../../models/user/update-usuario-request';

@Injectable({providedIn: 'root'})
export class UsuarioService {
    private readonly API_URL = enviroment.apiUrl;
    

    constructor(private http: HttpClient, private router: Router){}

    userList(): Observable<UsuarioResponse[]>{
        return this.http.get<UsuarioResponse[]>(`${this.API_URL}/usuarios`);
    }

    getUserById(id: number): Observable<UsuarioResponse>{
        return this.http.get<UsuarioResponse>(`${this.API_URL}/usuarios/${id}`);
    }

    updateUser(
        id: number,
        request: UpdateUsuarioRequest
    ): Observable<UsuarioResponse>{
        return this.http.put<UsuarioResponse>(`${this.API_URL}/usuarios/${id}`, request)
    }

    deleteUser(id:number): Observable<void>{
        return this.http.delete<void>(`${this.API_URL}/usuarios/${id}`);
    }
}
