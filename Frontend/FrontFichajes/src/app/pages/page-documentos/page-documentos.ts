// src/app/pages/page-justificantes/page-justificantes.ts
import { Component, inject, signal, computed, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { RouterLink } from '@angular/router';
import { SolicitudService } from '../../services/solicitud-service';
import { AuthService } from '../../services/auth-service';
import { IJustificante } from '../../interface/ijustificante';

declare var bootstrap: any;

@Component({
  selector: 'app-page-documentos',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './page-documentos.html',
  styleUrls: ['./page-documentos.css']
})
export class PageDocumentos implements OnInit {

  private solicitudService = inject(SolicitudService);
  private authService = inject(AuthService);
  private sanitizer = inject(DomSanitizer);

  justificantes = signal<IJustificante[]>([]);
  cargando = signal<boolean>(false);
  error = signal<string>('');
  filtroEstado = signal<string>('TODOS');
  pdfUrl = signal<SafeResourceUrl | null>(null);
  private modalInstance: any = null;

  esAdmin = signal<boolean>(false);

  estadosFiltro = [
    { value: 'TODOS', label: 'Todos' },
    { value: 'PENDIENTE', label: 'Pendientes' },
    { value: 'APROBADA', label: 'Aprobados' },
    { value: 'RECHAZADA', label: 'Rechazados' }
  ];

  tiposDocumento: Record<string, string> = {
    'JUSTIFICANTE_FALTA': 'Justificante de falta',
    'BAJA_MEDICA': 'Baja médica',
    'CITA_MEDICA': 'Cita médica',
    'MOD_FICHAJE': 'Modificación fichaje',
    'OTROS': 'Otros'
  };

  justificantesFiltrados = computed(() => {
    const filtro = this.filtroEstado();
    const lista = this.justificantes();

    if (filtro === 'TODOS') return lista;
    return lista.filter(j => j.estado === filtro);
  });

  totalPendientes = computed(() => {
    return this.justificantes().filter(j => j.estado === 'PENDIENTE').length;
  });

  totalAprobados = computed(() => {
    return this.justificantes().filter(j => j.estado === 'APROBADA').length;
  });

  totalRechazados = computed(() => {
    return this.justificantes().filter(j => j.estado === 'RECHAZADA').length;
  });

  ngOnInit(): void {
    this.esAdmin.set(this.authService.isAdmin());
    this.cargarJustificantes();
  }


  async cargarJustificantes(): Promise<void> {
    try {
      this.cargando.set(true);
      this.error.set('');

      const data = await this.solicitudService.obtenerJustificantes();

      this.justificantes.set(data || []);
      console.log('Justificantes cargados:', this.justificantes().length);

    } catch (err: any) {
      console.error('Error cargando justificantes:', err);
      this.error.set('Error al cargar justificantes: ' + (err.message || 'Error desconocido'));
    } finally {
      this.cargando.set(false);
    }
  }

  async abrirPdf(justificanteId: number): Promise<void> {

    const url = `/api/justificantes/${justificanteId}/ver`

    try {
      const response = await fetch(url, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      })

      if (response.ok) {
        const blob = await response.blob()

        const blobUrl = URL.createObjectURL(blob)
        window.open(blobUrl, '_blank');
        setTimeout(() => URL.revokeObjectURL(blobUrl), 1000)
      } else {
        const text = await response.text();
        this.error.set(`Error ${response.status}: ${text}`)
      }
    } catch (err) {
      this.error.set('Error al cargar el PDF')
    }
  }

  cerrarModal(): void {
    if (this.modalInstance) {
      this.modalInstance.hide();
      this.modalInstance = null;
    }
    this.pdfUrl.set(null);
  }

  getBadgeClass(estado: string): string {
    const clases: Record<string, string> = {
      'PENDIENTE': 'badge bg-warning text-dark',
      'APROBADA': 'badge bg-success',
      'RECHAZADA': 'badge bg-danger'
    };
    return clases[estado] || 'badge bg-secondary';
  }

  getTipoDocumentoLabel(tipo: string): string {
    return this.tiposDocumento[tipo] || tipo;
  }

  formatearFecha(fecha: string | Date): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

}