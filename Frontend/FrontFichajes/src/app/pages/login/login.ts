import { Component, inject } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { UserService } from '../../services/user-service';
import { IUser } from '../../interface/iuser';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  imports: [RouterLink, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private userService = inject(UserService)
  private router = inject(Router)

  ngOnInit(): void {
    if (localStorage.getItem('token')) {
      this.router.navigate(['/dashboard'])
    }
  }


  async getUser(loginForm: NgForm) {
    const loginUser: IUser = loginForm.value as IUser
    try {
      let res = await this.userService.login(loginUser)
      console.log(res)

      if (res.token) {
        localStorage.setItem('token', res.token)

        this.router.navigate(['/dashboard'])
        loginForm.reset()
      }

    } catch (error) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        background: '#0d2a4a',
        color:'white',
        confirmButtonColor:'#27ae60',
        text: "Credenciales incorrectas",
      });
      loginForm.reset();
    }
  }
}
