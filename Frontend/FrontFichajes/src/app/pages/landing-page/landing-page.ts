import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-landing-page',
  imports: [RouterLink],
  templateUrl: './landing-page.html',
  styleUrl: './landing-page.css',
})
export class LandingPage {
  
  isToken: boolean;

  constructor() {
    this.isToken = false;
  }

  ngOnInit(): void {
    if (localStorage.getItem('token')) {
      this.isToken = true;
    }
  }

}
