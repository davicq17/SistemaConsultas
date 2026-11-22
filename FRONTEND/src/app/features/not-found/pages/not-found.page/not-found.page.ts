import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  imports: [RouterLink],
  selector: 'app-not-found.page',
  styleUrl: './not-found.page.css',
  templateUrl: './not-found.page.html',
})
export class NotFoundPage {
  readonly url = inject(Router).url;
}
