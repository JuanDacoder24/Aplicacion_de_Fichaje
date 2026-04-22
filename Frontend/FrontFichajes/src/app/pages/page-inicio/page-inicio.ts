import { Component, inject, signal, OnInit, computed, OnDestroy } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { FichajeService } from '../../services/fichaje-service';
import { IUsuario } from '../../interface/iusuario';

@Component({
  selector: 'app-page-inicio',
  imports: [],
  templateUrl: './page-inicio.html',
  styleUrl: './page-inicio.css',
})
export class PageInicio implements OnInit, OnDestroy {

  private authService = inject(AuthService)
  private fichajeService = inject(FichajeService)

  usuario = signal<IUsuario | null>(null)
  fichando = signal<boolean>(false)
  tiempoSegundos = signal<number>(0)
  cargando = signal<boolean>(false)
  error = signal<string>('')

  private fichajeActivoId = signal<number | null>(null)
  private intervalo: any = null

  private horaEntrada: Date | null = null

  fechaHoy = computed(() =>
    new Date().toLocaleDateString('es-ES', {
      weekday: 'long',
      day: 'numeric',
      month: 'long'
    })
  )

  async ngOnInit() {
    const id = this.authService.id()

    if (id) {
      try {
        const data = await this.fichajeService.getUsuarioById(id)
        this.usuario.set(data)
        await this.verificarFichajeActivo(id)
      } catch (e) {
        console.error('Error al cargar usuario:', e)
      }
    }

    document.addEventListener('visibilitychange', this.onVisibilityChange)
  }

  private iniciarContador() {
    if (this.intervalo) clearInterval(this.intervalo)

    this.intervalo = setInterval(() => {
      if (this.horaEntrada) {
        const ahora = new Date()
        const segundos = Math.floor(
          (ahora.getTime() - this.horaEntrada.getTime()) / 1000
        )
        this.tiempoSegundos.set(segundos)
      }
    }, 1000)
  }

  

  private onVisibilityChange = async () => {
  if (!document.hidden) {
    const userId = this.authService.id()
    if (!userId) return

    const hoy = new Date().toISOString().split('T')[0]

    try {
      const fichaje = await this.fichajeService.getFichajeAbierto(userId, hoy)

      if (fichaje && fichaje.horaEntrada) {
        this.fichando.set(true)
        this.horaEntrada = new Date(fichaje.horaEntrada)

        const ahora = new Date()
        const segundos = Math.floor(
          (ahora.getTime() - this.horaEntrada.getTime()) / 1000
        )

        this.tiempoSegundos.set(segundos)
        this.iniciarContador()
      } else {
        this.fichando.set(false)
        this.horaEntrada = null
        this.tiempoSegundos.set(0)
      }

    } catch (e) {
      console.error('Error al recuperar fichaje activo:', e)
    }
  }
}

  async verificarFichajeActivo(usuarioId: number) {
    try {
      const hoy = new Date().toISOString().split('T')[0]
      const fichajes = await this.fichajeService.getFichajesByUsuario(usuarioId, 0, 100)

      const fichajeActivo = fichajes.content?.find((f: any) =>
        f.fecha === hoy && !f.horaSalida
      )

      if (fichajeActivo) {
        this.fichando.set(true)
        this.fichajeActivoId.set(fichajeActivo.id)

        if (fichajeActivo.horaEntrada) {
          this.horaEntrada = new Date(fichajeActivo.horaEntrada)
          const ahora = new Date()
          const segundos = Math.floor(
            (ahora.getTime() - this.horaEntrada.getTime()) / 1000
          )
          this.tiempoSegundos.set(segundos)

          this.iniciarContador()
        }
      }
    } catch (e) {
      console.error('Error al verificar fichaje activo:', e)
    }
  }

  tiempoFormateado(): string {
    const h = Math.floor(this.tiempoSegundos() / 3600)
    const m = Math.floor((this.tiempoSegundos() % 3600) / 60)
    const s = this.tiempoSegundos() % 60
    return `${this.pad(h)}:${this.pad(m)}:${this.pad(s)}`
  }

  private pad(n: number): string {
    return n.toString().padStart(2, '0')
  }

  async botonFichaje() {
    this.cargando.set(true)
    this.error.set('')

    try {
      const tipo = this.fichando() ? 'SALIDA' : 'ENTRADA'
      const respuesta = await this.fichajeService.createFichaje(tipo)

      if (!this.fichando()) {
        this.fichando.set(true)
        this.tiempoSegundos.set(0)
        this.fichajeActivoId.set(respuesta.id)

        this.horaEntrada = new Date()
        this.iniciarContador()

      } else {
        this.fichando.set(false)
        this.fichajeActivoId.set(null)

        clearInterval(this.intervalo)
        this.intervalo = null

        this.horaEntrada = null
      }

    } catch (e: any) {
      if (e.error && typeof e.error === 'string') {
        this.error.set(e.error)
      } else if (e.message) {
        this.error.set(e.message)
      } else {
        this.error.set('Error al registrar el fichaje')
      }

      if (this.error().includes('Ya tienes entrada abierta')) {
        await this.verificarFichajeActivo(this.authService.id())
      }

    } finally {
      this.cargando.set(false)
    }
  }

  ngOnDestroy() {
    if (this.intervalo) {
      clearInterval(this.intervalo)
    }

    document.removeEventListener('visibilitychange', this.onVisibilityChange)
  }
}