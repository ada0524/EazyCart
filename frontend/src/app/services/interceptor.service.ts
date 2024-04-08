import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class InterceptorService implements HttpInterceptor {
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // Some logic to grab the token
    const TOKEN = 'my jwt';

    // HttpHandler to clone our Request Object and append a token to the header
    return next.handle(req.clone({ setHeaders: { TOKEN } }));
  }

  constructor() {}
}
