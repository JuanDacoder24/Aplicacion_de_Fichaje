import { Component, inject, OnInit } from '@angular/core';
import { IHorarios } from '../../interface/ihorarios';
import { IUsuario } from '../../interface/iusuario';
import { FichajeService } from '../../services/fichaje-service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-page-horarios',
  imports: [FormsModule],
  templateUrl: './page-horarios.html',
  styleUrl: './page-horarios.css',
})
export class PageHorarios implements OnInit {

  private fichajeService = inject(FichajeService) 

  horarios: IHorarios [] = []
  usuarios: IUsuario [] = []  

  nuevoHorario: any = {
    usuarioId: '',
    diaSemana: '',
    horaInicio: '',
    horaFin: ''
  }

  dias = ['LUNES','MARTES','MIERCOLES','JUEVES','VIERNES','SABADO','DOMINGO']

  constructor() { }

  async ngOnInit() {
    this.cargarHorarios()
    this.cargarUsuarios()
  }

  async cargarHorarios() {
  this.horarios = await this.fichajeService.getHorarios()
  console.log('HORARIOS:', this.horarios)
  }

  async cargarUsuarios() {
    this.usuarios = await this.fichajeService.getUsuarios()
  }

  async guardar() {
  try {
    const horarioParaEnviar = {
      usuarioId: Number(this.nuevoHorario.usuarioId),
      diaSemana: this.nuevoHorario.diaSemana,
      horaInicio: this.nuevoHorario.horaInicio + ':00', 
      horaFin: this.nuevoHorario.horaFin + ':00'
    };
    
    console.log('Enviando horario:', horarioParaEnviar);
    await this.fichajeService.createHorario(horarioParaEnviar);
    await this.cargarHorarios(); 
    
    this.nuevoHorario = {
      usuarioId: '',
      diaSemana: '',
      horaInicio: '',
      horaFin: ''
    };
    
  } catch (e) {
    console.error('Error al guardar:', e);
    alert('Error al guardar el horario');
  }
}

  async eliminar(id: number) {
    await this.fichajeService.deleteHorario(id)
    this.cargarHorarios()
  }
}

