import { Component, inject, signal } from '@angular/core';
import { FichajeService } from '../../services/fichaje-service';
import { AuthService } from '../../services/auth-service';
import { ISolicitudes } from '../../interface/isolicitudes';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-page-solicitudes',
  imports: [CommonModule],
  templateUrl: './page-solicitudes.html',
  styleUrl: './page-solicitudes.css',
})
export class PageSolicitudes {



  private authService = inject(AuthService)
  private fichajeService = inject(FichajeService)

  solicitudes = signal<ISolicitudes[]>([])

  async ngOnInit() {
  const id = this.authService.id()

  if (id) {
    try {
      const data = await this.fichajeService.getSolicitudes()
      this.solicitudes.set(data)
    } catch (e) {
      console.error('Error al cargar las solicitudes: ', e)
    }
  }
}

  formatHora(fecha: Date | string | null): string {
    if (!fecha) return '—'
    return new Date(fecha).toLocaleTimeString('es-ES', {
      hour: '2-digit', minute: '2-digit'
    })
  }

  formatFecha(fecha: Date | string | null): string {
    if (!fecha) return '—'
    return new Date(fecha).toLocaleDateString('es-ES', {
      day: '2-digit', month: '2-digit', year: 'numeric'
    })
  }

  async rechazar(id: number) {
    try {
      await this.fichajeService.deleteFichaje(id)
      this.solicitudes.update(list => list.filter(f => f.id !== id))
    } catch (e) {
      Swal.fire({
        icon: "error",
        title: "Oops...",
        background: '#0d2a4a',
        color: 'white',
        confirmButtonColor: '#27ae60',
        text: "No se pudo eliminar la solicitud",
      });
    }
  }

  async aprobar(id: number) {
  try {
    // Asumiendo que tienes un método para aprobar
    // await this.fichajeService.aprobarSolicitud(id)
    
    // Actualizar el estado localmente
    this.solicitudes.update(list => 
      list.map(solicitud => 
        solicitud.id === id 
          ? { ...solicitud, estado: 'Aprobada' } 
          : solicitud
      )
    )
    
    Swal.fire({
      icon: "success",
      title: "¡Aprobada!",
      background: '#0d2a4a',
      color: 'white',
      confirmButtonColor: '#27ae60',
      text: "Solicitud aprobada correctamente",
    });
  } catch (e) {
    Swal.fire({
      icon: "error",
      title: "Oops...",
      background: '#0d2a4a',
      color: 'white',
      confirmButtonColor: '#27ae60',
      text: "No se pudo aprobar la solicitud",
    });
  }
}

}
