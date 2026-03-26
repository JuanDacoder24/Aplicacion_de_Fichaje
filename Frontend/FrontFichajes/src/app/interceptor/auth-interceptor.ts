import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes("login")) {
    return next(req);
  }

  const token = localStorage.getItem("token");
  const cloneRequest = req.clone({
    setHeaders: {
      'Content-Type': 'application/json',
      ...(token && { 'Authorization': `Bearer ${token}` })
    }
  });

  return next(cloneRequest);
};
