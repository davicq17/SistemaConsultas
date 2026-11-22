import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (_route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('access_token');

  if(token){
    return true;
  }

  return router.createUrlTree(['/auth/login'],{
    queryParams: { returnUrl: state.url }
  });
};
