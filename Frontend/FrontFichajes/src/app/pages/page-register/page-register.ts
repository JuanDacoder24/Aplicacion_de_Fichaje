import { UserService } from './../../services/user-service';
import { Component, inject } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IUsuario } from '../../interface/iusuario';

@Component({
  selector: 'app-page-register',
  imports: [],
  templateUrl: './page-register.html',
  styleUrl: './page-register.css',
})
export class PageRegister {

  private userService = inject(UserService)
  private router = inject(Router)

  registerForm: FormGroup
  private nextId: number
  isCargando: boolean = false

  rol = [
    { id: 1, nombre: 'Admin' },
    { id: 2, nombre: 'Empleado' }
  ]

  departamento = [
    { id: 1, nombre: 'RRHH' },
    { id: 2, nombre: 'IT' },
    { id: 3, nombre: 'VENTAS' },
    { id: 4, nombre: 'MARKETING' }
  ]

  constructor() {
    this.nextId = 1
    this.registerForm = new FormGroup({
      id: new FormControl(-1),
      nombre: new FormControl(null, [Validators.required, Validators.minLength(3), Validators.maxLength(100)]),
      email: new FormControl(null, [Validators.required, Validators.pattern(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/)]),
      passwordHash: new FormControl(null, [Validators.required, Validators.minLength(6)]),
      repitePassword: new FormControl(null, [Validators.required, Validators.minLength(6)]),
      rol: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(2)]),
      departamento: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(7)]),
      fechaAlta: new FormControl(Date.now()),
      activo: new FormControl(true)
    }, [this.passValidator])
  }

  ngOnInit(): void {

  }

  // Metodo para obtener los datos del formulario
  async getDataForm() {
    if (this.registerForm.invalid) {
      Object.keys(this.registerForm.controls).forEach(key => {
        this.registerForm.get(key)?.markAsTouched()
      })
      return
    }

    const usuario: IUsuario = {
      id: 0, 
      nombre: this.registerForm.get('nombre')?.value,
      email: this.registerForm.get('email')?.value,
      passwordHash: this.registerForm.get('passwordHash')?.value,
      rol: parseInt(this.registerForm.get('rol')?.value),
      departamento: parseInt(this.registerForm.get('departamento')?.value),
      fechaAlta: Date.now(),
      activo: this.registerForm.get('activo')?.value
    }

    try {
      const response = await this.userService.register(usuario)
      console.log('Respuesta:', response)
      this.resetForm()

      setTimeout(() => {
        this.router.navigate(['/dashboard/pageDocumentos'])
      }, 2000)

    } catch (error: any) {
      console.error('Error:', error)
    }
  }

  // Resetear formulario
  resetForm() {
    this.registerForm.reset({
      id: -1,
      nombre: null,
      email: null,
      passwordHash: null,
      repitePassword: null,
      rol: null,
      departamento: null,
      fechaAlta: Date.now(),
      activo: true
    })

    // Marcar todos los campos como no tocados
    Object.keys(this.registerForm.controls).forEach(key => {
      const control = this.registerForm.get(key)
      control?.markAsUntouched()
    })
  }

  // Validador personalizado para verificar que las contraseñas coincidan
  passValidator(formValue: AbstractControl): any {
    const password = formValue.get('passwordHash')?.value
    const repitePassword = formValue.get('repitePassword')?.value

    return (password !== repitePassword) ? { 'passwordnotmatches': true } : null
  }

  // Metodo para verificar errores en controles especificos
  checkControl(formControlName: string, validator: string): boolean | undefined {
    return this.registerForm.get(formControlName)?.hasError(validator) &&
      this.registerForm.get(formControlName)?.touched
  }


}
