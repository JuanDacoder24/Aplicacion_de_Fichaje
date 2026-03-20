import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  if (req.url.includes("login")) {
    return next(req);
  }

  const cloneRequest = req.clone({
    setHeaders: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${localStorage.getItem("token") || ""}`
    }
  });

  return next(cloneRequest);
};
