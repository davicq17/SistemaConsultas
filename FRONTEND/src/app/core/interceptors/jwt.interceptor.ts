import { HttpInterceptorFn } from "@angular/common/http";

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
    const token = localStorage.getItem('access_token');

    if(!token){
        return next(req);
    }

    if(req.headers.has('Authorization')){
        return next(req);
    }

    const authReq = req.clone ({
        setHeaders:{
            Authorization: `Bearer ${token}`
        }
    });

    return next(authReq);
}