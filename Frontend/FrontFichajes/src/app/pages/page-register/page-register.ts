import { AuthService } from './../../services/auth-service';
import { Component, inject, signal, OnInit } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { IUsuario } from '../../interface/iusuario';
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

  // Signals para estado reactivo
  public registerForm = signal<FormGroup>(new FormGroup({
    id: new FormControl(-1),
    nombre: new FormControl(null, [Validators.required, Validators.minLength(3), Validators.maxLength(100)]),
    email: new FormControl(null, [Validators.required, Validators.pattern(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/)]),
    passwordHash: new FormControl(null, [Validators.required, Validators.minLength(6)]),
    repitePassword: new FormControl(null, [Validators.required, Validators.minLength(6)]),
    rol: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(2)]),
    departamento: new FormControl(null, [Validators.required, Validators.min(1), Validators.max(7)]),
    fechaAlta: new FormControl(Date.now()),
    activo: new FormControl(true)
  }, [this.passValidator]));

  public usuarios = signal<IUsuario[]>([]);
  public isCargando = signal<boolean>(false);
  public nextId = signal<number>(1);

  // Datos estáticos (también podrían ser signals si cambian dinámicamente)
  public rol = [
    { id: 1, nombre: 'Admin' },
    { id: 2, nombre: 'Empleado' }
  ];

  public departamento = [
    { id: 1, nombre: 'RRHH' },
    { id: 2, nombre: 'IT' },
    { id: 3, nombre: 'VENTAS' },
    { id: 4, nombre: 'MARKETING' }
  ];

  ngOnInit(): void {
    this.cargarUsuarios();
  }

  // Método para obtener los datos del formulario
  async getDataForm() {
    const form = this.registerForm();
    
    if (form.invalid) {
      Object.keys(form.controls).forEach(key => {
        form.get(key)?.markAsTouched();
      });
      return;
    }

    this.isCargando.set(true);

    const usuario: IUsuario = {
      id: this.nextId(),
      nombre: form.get('nombre')?.value,
      email: form.get('email')?.value,
      passwordHash: form.get('passwordHash')?.value,
      rol: parseInt(form.get('rol')?.value),
      departamento: parseInt(form.get('departamento')?.value),
      fechaAlta: Date.now(),
      activo: form.get('activo')?.value
    };

    try {
      const response = await this.authService.register(usuario);
      console.log('Respuesta:', response);
      
      // Actualizar señales
      this.usuarios.update(prev => [...prev, usuario]);
      this.nextId.update(id => id + 1);
      this.resetForm();

      setTimeout(() => {
        this.router.navigate(['/dashboard/pageDocumentos']);
      }, 2000);
    } catch (error) {
      console.error('Error al registrar:', error);
    } finally {
      this.isCargando.set(false);
    }
  }

  getDepartamento(id: number): string {
    return this.departamento.find(d => d.id === id)?.nombre || '-';
  }

  // Resetear formulario
  resetForm() {
    const form = this.registerForm();
    form.reset({
      id: -1,
      nombre: null,
      email: null,
      passwordHash: null,
      repitePassword: null,
      rol: null,
      departamento: null,
      fechaAlta: Date.now(),
      activo: true
    });

    // Marcar todos los campos como no tocados
    Object.keys(form.controls).forEach(key => {
      const control = form.get(key);
      control?.markAsUntouched();
    });
  }

  // Validador personalizado para verificar que las contraseñas coincidan
  passValidator(formValue: AbstractControl): any {
    const password = formValue.get('passwordHash')?.value;
    const repitePassword = formValue.get('repitePassword')?.value;
    return (password !== repitePassword) ? { 'passwordnotmatches': true } : null;
  }

  // Método para verificar errores en controles específicos
  checkControl(formControlName: string, validator: string): boolean | undefined {
    const control = this.registerForm().get(formControlName);
    return control?.hasError(validator) && control?.touched;
  }

  // Cargar usuarios existentes (ejemplo)
  private cargarUsuarios() {
    // Aquí cargarías usuarios desde un servicio si es necesario
    // this.usuarios.set(usuariosExistentes);
  }
}