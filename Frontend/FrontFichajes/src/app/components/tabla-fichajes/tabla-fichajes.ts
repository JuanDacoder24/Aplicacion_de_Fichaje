import { Component, input, output } from '@angular/core';
import { IFichajes } from '../../interface/ifichajes';

@Component({
  selector: 'app-tabla-fichajes',
  imports: [],
  templateUrl: './tabla-fichajes.html',
  styleUrl: './tabla-fichajes.css',
})
export class TablaFichajes {

  empleado = input.required<string>();
  fichajes = input.required<IFichajes[]>();

  onEditar = output<IFichajes>();
  onEliminar = output<number>();

  nombresDepartamentos: { [key: number]: string } = {
    1: 'Sistemas',
    2: 'Recursos Humanos',
    3: 'Ventas',
    4: 'Marketing'
  };

  formatHora(fecha: Date | string | null): string {
    if (!fecha) return '—'
    return new Date(fecha).toLocaleTimeString('es-ES', { hour: '2-digit', minute: '2-digit' })
  }

  formatFecha(fecha: Date | string | null): string {
    if (!fecha) return '—'
    return new Date(fecha).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' })
  }

  formatHoras(h: number | null): string {
    if (h == null) return '—'
    const hh = Math.floor(h)
    const mm = Math.round((h - hh) * 60)
    return `${hh}h ${mm.toString().padStart(2, '0')}m`
  }

  getNombreDepartamento(f: IFichajes): string {
    const dep = f.usuario?.departamento;
    return (dep && this.nombresDepartamentos[dep]) ? this.nombresDepartamentos[dep] : 'General';
  }

}
