import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const roleGuard = (allowedRoles: string[]): CanActivateFn  => {
  return (_routes, _states) => {
    
    const router = inject(Router)
    const rol = localStorage.getItem('rol');
    const token = localStorage.getItem('access_token'); 

    if(!token){
      return router.createUrlTree(['auth/login']);
    }

    if(rol && allowedRoles.includes(rol)){
      return true;
    }

    return router.createUrlTree(['/forbidden']);
  };
};
