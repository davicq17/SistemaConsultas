import { HttpErrorResponse, HttpInterceptorFn } from "@angular/common/http";
import { inject, } from "@angular/core";
import { Router } from "@angular/router";
import {catchError, throwError} from 'rxjs';

import { AuthService } from "../services/auth.service";
import { ApiError } from "../models/api-error";

function getApiError(error: HttpErrorResponse): ApiError | null {
    const body: unknown = error.error;

    if (
        body !== null &&
        typeof body === 'object' &&
        'message' in body
    ) {
        return body as ApiError;
    }

    return null;
}

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
    const router = inject(Router);
    const authService = inject(AuthService);

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            const apiError = getApiError(error);

            switch (error.status){
                case 0:
                    console.error(
                        'NO se pudo conectar con la API. Revisa conexión, URL y CORS.'
                    );
                    break;
                
                case 400:
                    console.warn(
                        apiError?.message ?? 'La solicitud no es válida.'
                    );
                    break;

                case 401:
                    if(!req.url.includes('/auth/login')){
                        authService.logout();
                    }
                    break;

                case 403:
                    void router.navigate(['forbidden']);
                    break;

                case 404:
                    console.warn(
                        apiError?.message?? 'El recurso no existe.'
                    );
                    break;

                default:
                    if(error.status >= 500) {
                        console.error(
                            'Error del servidor:',
                            apiError?.message?? 'Ocurrio un error interno.'
                        );
                    }
            }

            return throwError(() => error);
        })
    )
}