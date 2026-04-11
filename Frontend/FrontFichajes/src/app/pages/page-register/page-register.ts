import { AuthService } from './../../services/auth-service';
import { Component, inject, signal, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IUsuario, IUsuarioRegistro } from '../../interface/iusuario';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-page-register',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './page-register.html',
  styleUrl: './page-register.css',
})
export class PageRegister implements OnInit {

  private authService = inject(AuthService);
  private router = inject(Router);

  public registerForm = signal<FormGroup>(new FormGroup({
    nombre: new FormControl(null, [Validators.required, Validators.minLength(3), Validators.maxLength(100)]),
    email: new FormControl(null, [Validators.required, Validators.pattern(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/)]),
    passwordHash: new FormControl(null, [Validators.required, Validators.minLength(6)]),
    repitePassword: new FormControl(null, [Validators.required, Validators.minLength(6)]),
    rol: new FormControl(null, [Validators.required]),
    departamento: new FormControl(null, [Validators.required]),
    activo: new FormControl(true)
  }, [this.passValidator]));

  public usuarios = signal<IUsuario[]>([]);
  public isCargando = signal<boolean>(false);
  public errorMensaje = signal<string>('');

  public roles = [
    { id: 1, nombre: 'Admin' },
    { id: 2, nombre: 'Empleado' }
  ];

  public departamentos = [
    { id: 1, nombre: 'RRHH' },
    { id: 2, nombre: 'IT' },
    { id: 3, nombre: 'VENTAS' },
    { id: 4, nombre: 'MARKETING' }
  ];

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  async getDataForm() {
    const form = this.registerForm();

    if (form.invalid) {
      Object.keys(form.controls).forEach(key => {
        form.get(key)?.markAsTouched();
      });
      return;
    }

    this.isCargando.set(true);
    this.errorMensaje.set('');

    // Solo enviamos los datos necesarios, el id y fechaAlta los gestiona el backend
    const usuario = {
      nombre: form.get('nombre')?.value,
      email: form.get('email')?.value,
      passwordHash: form.get('passwordHash')?.value,
      rol: parseInt(form.get('rol')?.value),
      departamento: parseInt(form.get('departamento')?.value),
      activo: form.get('activo')?.value ?? true
    };

    try {
      const response = await this.authService.register(usuario);
      console.log('Usuario registrado:', response);

      // Recargamos la lista desde el backend para tener datos reales
      await this.cargarUsuarios();
      this.resetForm();

      setTimeout(() => {
        this.router.navigate(['/dashboard/pageDocumentos']);
      }, 2000);

    } catch (error: any) {
      console.error('Error al registrar:', error);
      this.errorMensaje.set(error?.error || 'Error al registrar el usuario');
    } finally {
      this.isCargando.set(false);
    }
  }

  getRolNombre(id: number): string {
    return this.roles.find(r => r.id === id)?.nombre || '-';
  }

  getDepartamento(id: number): string {
    return this.departamentos.find(d => d.id === id)?.nombre || '-';
  }

  resetForm() {
    const form = this.registerForm();
    form.reset({
      nombre: null,
      email: null,
      passwordHash: null,
      repitePassword: null,
      rol: null,
      departamento: null,
      activo: true
    });

    Object.keys(form.controls).forEach(key => {
      form.get(key)?.markAsUntouched();
    });
  }

  passValidator(formValue: AbstractControl): any {
    const password = formValue.get('passwordHash')?.value;
    const repitePassword = formValue.get('repitePassword')?.value;
    return (password !== repitePassword) ? { 'passwordnotmatches': true } : null;
  }

  checkControl(formControlName: string, validator: string): boolean | undefined {
    const control = this.registerForm().get(formControlName);
    return control?.hasError(validator) && control?.touched;
  }

  // Carga los usuarios reales desde el backend
  private async cargarUsuarios() {
    try {
      const usuarios = await this.authService.getUsuarios();
      this.usuarios.set(usuarios);
    } catch (error) {
      console.error('Error al cargar usuarios:', error);
    }
  }
}