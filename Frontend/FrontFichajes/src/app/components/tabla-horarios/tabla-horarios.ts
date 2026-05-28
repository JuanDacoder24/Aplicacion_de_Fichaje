import { Component, input, output } from '@angular/core';
import { IHorarios } from '../../interface/ihorarios';

@Component({
  selector: 'app-tabla-horarios',
  imports: [],
  templateUrl: './tabla-horarios.html',
  styleUrl: './tabla-horarios.css',
})
export class TablaHorarios {

  empleado = input.required<string>()
  turnos = input.required<IHorarios[]>()

  onEliminar = output<number>();

  eliminarHorario(id: number) {
    this.onEliminar.emit(id);
  }
}
