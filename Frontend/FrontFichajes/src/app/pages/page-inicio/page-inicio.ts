import { Component, inject, signal, OnInit, computed } from '@angular/core';
import { AuthService } from '../../services/auth-service';
import { FichajeService } from '../../services/fichaje-service';
import { IUsuario } from '../../interface/iusuario';

@Component({
  selector: 'app-page-inicio',
  imports: [],
  templateUrl: './page-inicio.html',
  styleUrl: './page-inicio.css',
})
export class PageInicio implements OnInit {  

  private authService = inject(AuthService)
  private fichajeService = inject(FichajeService)

  usuario = signal<IUsuario | null>(null)
  fichando = signal<boolean>(false)
  tiempoSegundos = signal<number>(0)
  cargando = signal<boolean>(false)
  error = signal<string>('')
  private fichajeActivoId = signal<number | null>(null)  

  private intervalo: any = null

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
  }

  fechaHoy = computed(() => new Date().toLocaleDateString('es-ES', { weekday: 'long', day: 'numeric', month: 'long' }));
  
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
          const horaEntrada = new Date(fichajeActivo.horaEntrada)
          const ahora = new Date()
          const segundosTranscurridos = Math.floor((ahora.getTime() - horaEntrada.getTime()) / 1000)
          this.tiempoSegundos.set(segundosTranscurridos)
          this.intervalo = setInterval(() => {
            this.tiempoSegundos.update(t => t + 1)
          }, 1000)
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
        this.intervalo = setInterval(() => {
          this.tiempoSegundos.update(t => t + 1)
        }, 1000)
        this.error.set('')  
      } else {
        this.fichando.set(false)
        this.fichajeActivoId.set(null)
        clearInterval(this.intervalo)
        this.intervalo = null
        this.error.set('')
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
  }
}