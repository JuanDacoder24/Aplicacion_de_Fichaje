import { Component, inject, signal } from '@angular/core'
import { FichajeService } from '../../services/fichaje-service'
import { AuthService } from '../../services/auth-service'
import { EstadisticasSolicitudesDTO, FiltroSolicitudesDTO, ISolicitudes, RevisionSolicitudDTO } from '../../interface/isolicitudes'
import { CommonModule } from '@angular/common'
import { HttpClient, HttpParams } from '@angular/common/http'
import { firstValueFrom } from 'rxjs'
import { IJustificante } from '../../interface/ijustificante'

@Component({
  selector: 'app-page-solicitudes',
  imports: [CommonModule],
  templateUrl: './page-solicitudes.html',
  styleUrl: './page-solicitudes.css',
})
export class PageSolicitudes {

  esAdmin = false
  archivo: File | null = null
  filtroEstado = 'TODAS'
  comentarioAdmin = ''

  private http = inject(HttpClient)

  private apiUrl = '/api/solicitudes'
  private justificantesUrl = '/api/justificantes'

  private authService = inject(AuthService)
  private fichajeService = inject(FichajeService)

  solicitudes = signal<ISolicitudes[]>([])

  nuevaSolicitud = {
    tipoDocumento: 'JUSTIFICANTE_FALTA',
    fichajeId: null as number | null,
    motivo: ''
  }

  tiposDocumento = [
    { value: 'JUSTIFICANTE_FALTA', label: 'Justificante de falta' },
    { value: 'BAJA_MEDICA',        label: 'Baja médica' },
    { value: 'CITA_MEDICA',        label: 'Cita médica' },
    { value: 'MOD_FICHAJE',        label: 'Modificación fichaje' },
    { value: 'OTROS',              label: 'Otros' }
  ]

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

  
  

  
  

  
}
