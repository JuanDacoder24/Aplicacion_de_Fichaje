import { Component, inject, signal, computed, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SolicitudService } from '../../services/solicitud-service';
import { AuthService } from '../../services/auth-service';
import { FichajeService } from '../../services/fichaje-service';
import { ISolicitudes } from '../../interface/isolicitudes';
import { IJustificante } from '../../interface/ijustificante';
import { IUsuario } from '../../interface/iusuario';
import { IFichajes } from '../../interface/ifichajes';

@Component({
  selector: 'app-page-solicitudes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './page-solicitudes.html',
  styleUrls: ['./page-solicitudes.css']
})
export class PageSolicitudes implements OnInit, OnDestroy {

  private solicitudService = inject(SolicitudService)
  private authService = inject(AuthService)
  private fichajeService = inject(FichajeService)
  private sanitizer = inject(DomSanitizer)
  private cargandoSolicitudes = false

  cargando = signal<boolean>(false)
  enviando = signal<boolean>(false)
  error = signal<string>('')
  exito = signal<string>('')
  solicitudes = signal<ISolicitudes[]>([])
  justificantes = signal<IJustificante[]>([])
  fichajes = signal<IFichajes[]>([])
  usuarioActual = signal<IUsuario | null>(null)
  solicitudesPropias = signal<ISolicitudes[]>([])


  esAdmin = signal<boolean>(false)
  filtroEstado = signal<string>('TODAS')
  comentariosAdmin = signal<Record<number, string>>({})



  nuevaSolicitud = signal({
    tipoDocumento: 'OTROS',
    motivo: '',
    fichajeId: null as number | null
  })

  archivoSeleccionado: File | null = null
  nombreArchivo = signal<string>('')

  pdfUrl = signal<SafeResourceUrl | null>(null)
  private modalInstance: any = null
  solicitudesFiltradas = computed(() => {
    const filtro = this.filtroEstado()
    const lista = this.solicitudes()

    if (filtro === 'TODAS') return lista
    return lista.filter(s => s.estado === filtro)
  })

  totalPendientes = computed(() => {
    return this.solicitudes().filter(s => s.estado === 'PENDIENTE').length
  })

  totalResueltas = computed(() => {
    return this.solicitudes().filter(s => s.estado === 'APROBADA' || s.estado === 'RECHAZADA').length
  })

  tiposDocumento = [
    { value: 'JUSTIFICANTE_FALTA', label: 'Justificante de falta' },
    { value: 'BAJA_MEDICA', label: 'Baja médica' },
    { value: 'CITA_MEDICA', label: 'Cita médica' },
    { value: 'MOD_FICHAJE', label: 'Modificación fichaje' },
    { value: 'OTROS', label: 'Otros' },
  ]

  estadosFiltro = [
    { value: 'TODAS', label: 'Todas' },
    { value: 'PENDIENTE', label: 'Pendientes' },
    { value: 'APROBADA', label: 'Aprobadas' },
    { value: 'RECHAZADA', label: 'Rechazadas' }
  ]

  ngOnInit(): void {
    this.inicializarComponente()
  }

  ngOnDestroy(): void {
    this.cerrarModal()
  }

  async cargarSolicitudes(): Promise<void> {
    if (this.cargandoSolicitudes) {
      console.log('Ya está cargando solicitudes, evitando bucle');
      return;
    }

    try {
      this.cargandoSolicitudes = true;

      if (this.esAdmin()) {
        const todas = await this.solicitudService.obtenerSolicitudes();
        this.solicitudes.set(todas || []);
        const userId = this.authService.id();
        const solicitudesDelAdmin = (todas || []).filter(s => s.usuario?.id === userId);
        this.solicitudesPropias.set(solicitudesDelAdmin);
      } else {
        const propias = await this.solicitudService.obtenerMisSolicitudes();
        this.solicitudes.set(propias || []);
        this.solicitudesPropias.set(propias || []);
      }

    } catch (err) {
      console.error('Error cargando solicitudes:', err)
      this.error.set('Error al cargar solicitudes: ' + (err as any).message);
    } finally {
      this.cargandoSolicitudes = false;
    }
  }

  async inicializarComponente(): Promise<void> {
    try {
      this.cargando.set(true);

      const esAdmin = this.authService.isAdmin();
      console.log('esAdmin desde authService:', esAdmin);

      this.esAdmin.set(esAdmin);

      const userId = this.authService.id();
      const userName = this.authService.nombre();
      const userRole = this.authService.rol();

      console.log('Datos usuario:', { userId, userName, userRole, esAdmin });


      await Promise.all([
        this.cargarSolicitudes(),
        this.cargarFichajes()
      ]);

      if (this.esAdmin()) {
        await this.cargarJustificantes()
      }

    } catch (err) {
      console.error('Error al inicializar:', err)
    } finally {
      this.cargando.set(false);
    }
  }

  async cargarJustificantes(): Promise<void> {
    try {
      const data = await this.solicitudService.obtenerJustificantes()
      this.justificantes.set(data)
    } catch (err) {
      console.error('Error cargando justificantes:', err)
    }
  }

  async cargarFichajes(): Promise<void> {
    try {
      const data = await this.fichajeService.obtenerFichajesUsuario()
      this.fichajes.set(data)
    } catch (err) {
      console.error('Error cargando fichajes:', err)
    }
  }


  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement

    if (input.files && input.files.length > 0) {
      const file = input.files[0]

      if (file.type !== 'application/pdf') {
        this.error.set('Solo se permiten archivos PDF')
        this.limpiarArchivo()
        return
      }
      if (file.size > 5 * 1024 * 1024) {
        this.error.set('El archivo no puede superar los 5MB')
        this.limpiarArchivo()
        return
      }

      this.archivoSeleccionado = file
      this.nombreArchivo.set(file.name)
      this.error.set('')
    }
  }

  limpiarArchivo(): void {
    this.archivoSeleccionado = null
    this.nombreArchivo.set('')
  }

  async enviarSolicitud(): Promise<void> {
    const solicitud = this.nuevaSolicitud();

    if (!solicitud.motivo || !solicitud.motivo.trim()) {
      this.error.set('El motivo es obligatorio');
      console.log('Error: motivo vacío');
      return;
    }

    try {
      this.enviando.set(true);
      this.error.set('');

      const fichajeIdParaEnviar = solicitud.fichajeId ? Number(solicitud.fichajeId) : undefined;

      let solicitudCreada: ISolicitudes;
      solicitudCreada = await this.solicitudService.crearSolicitud(
        solicitud.motivo,
        fichajeIdParaEnviar
      );

      console.log('Solicitud creada:', solicitudCreada);

      if (this.archivoSeleccionado && solicitudCreada.id) {
        console.log('Subiendo justificante...');
        await this.solicitudService.subirJustificante(
          this.archivoSeleccionado,
          solicitud.tipoDocumento,
          solicitudCreada.id,
          fichajeIdParaEnviar
        );
      }

      this.nuevaSolicitud.set({
        tipoDocumento: 'OTROS',
        motivo: '',
        fichajeId: null
      });
      this.limpiarArchivo();

      this.exito.set('Solicitud enviada correctamente');
      console.log('Solicitud completada con éxito');

      await this.cargarSolicitudes();

      setTimeout(() => {
        if (this.exito() === 'Solicitud enviada correctamente') {
          this.exito.set('');
        }
      }, 3000);

    } catch (err: any) {
      this.error.set(err.message || err.error || 'Error al enviar la solicitud');
    } finally {
      this.enviando.set(false);
    }
  }

  async aprobarSolicitud(solicitud: ISolicitudes): Promise<void> {
    if (solicitud.estado !== 'PENDIENTE') {
      this.error.set('Esta solicitud ya fue revisada');
      return;
    }

    const comentario = this.comentariosAdmin()[solicitud.id] || ''

    try {
      this.cargando.set(true)

      console.log(`Aprobando solicitud ${solicitud.id} con comentario:`, comentario);

      await this.solicitudService.revisarSolicitud(
        solicitud.id,
        'APROBADA',
        comentario
      )

      this.exito.set('Solicitud aprobada correctamente')
      await this.cargarSolicitudes()

      const nuevosComentarios = { ...this.comentariosAdmin() }
      delete nuevosComentarios[solicitud.id]
      this.comentariosAdmin.set(nuevosComentarios)

    } catch (err: any) {
      console.error('Error aprobando solicitud:', err)
      this.error.set(err.message || 'Error al aprobar la solicitud')
    } finally {
      this.cargando.set(false)
      setTimeout(() => this.exito.set(''), 3000)
    }
  }

  async rechazarSolicitud(solicitud: ISolicitudes): Promise<void> {
    const comentario = this.comentariosAdmin()[solicitud.id] || ''

    try {
      this.cargando.set(true)

      await this.solicitudService.revisarSolicitud(
        solicitud.id,
        'RECHAZADA',
        comentario
      )

      this.exito.set('Solicitud rechazada')

      const nuevosComentarios = { ...this.comentariosAdmin() }
      delete nuevosComentarios[solicitud.id]
      this.comentariosAdmin.set(nuevosComentarios)

      await this.cargarSolicitudes()
      await this.cargarJustificantes()

      setTimeout(() => {
        if (this.exito() === 'Solicitud rechazada') {
          this.exito.set('')
        }
      }, 3000)

    } catch (err: any) {
      console.error('Error rechazando solicitud:', err)
      this.error.set(err.message || 'Error al rechazar la solicitud')
    } finally {
      this.cargando.set(false)
    }
  }


  abrirModalPdf(justificanteId: number): void {
    try {
      const url = this.solicitudService.verPdf(justificanteId)
      this.pdfUrl.set(this.sanitizer.bypassSecurityTrustResourceUrl(url))
      const modalElement = document.getElementById('modalPdf')
      if (modalElement) {

        window.open(url, '_blank')
      } else {
        window.open(url, '_blank')
      }
    } catch (err) {
      console.error('Error abriendo PDF:', err)
      this.error.set('Error al abrir el PDF')
    }
  }

  cerrarModal(): void {
    if (this.modalInstance) {
      this.modalInstance.hide()
      this.modalInstance = null
    }
    this.pdfUrl.set(null)
  }


  getJustificanteBySolicitud(solicitudId: number): IJustificante | undefined {
    return this.justificantes().find(j => j.solicitudId === solicitudId)
  }

  getBadgeClass(estado: string): string {
    const clases: Record<string, string> = {
      'PENDIENTE': 'badge bg-warning text-dark',
      'APROBADA': 'badge bg-success',
      'RECHAZADA': 'badge bg-danger'
    };
    return clases[estado] || 'badge bg-secondary';
  }

  getNombreUsuario(usuario?: IUsuario): string {
    if (!usuario) return 'Usuario'
    return usuario.nombre || usuario.email || 'Usuario'
  }

  formatearFecha(fecha: Date | string): string {
    if (!fecha) return ''
    const date = new Date(fecha)
    return date.toLocaleDateString('es-ES', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }
}