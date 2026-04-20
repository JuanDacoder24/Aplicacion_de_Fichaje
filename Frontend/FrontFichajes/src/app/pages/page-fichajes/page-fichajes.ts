import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { FichajeService } from '../../services/fichaje-service';
import { IFichajes } from '../../interface/ifichajes';
import { IUsuario } from '../../interface/iusuario';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-page-fichajes',
  imports: [CommonModule, FormsModule],
  templateUrl: './page-fichajes.html',
  styleUrl: './page-fichajes.css',
})
export class PageFichajes implements OnInit {

  private fichajeService = inject(FichajeService)

  fichajes = signal<IFichajes[]>([])
  usuarios = signal<IUsuario[]>([])
  cargando = signal(false)
  error = signal('')

  nombresDepartamentos: { [key: number]: string } = {
    1: 'Sistemas',
    2: 'Recursos Humanos',
    3: 'Ventas',
    4: 'Marketing'
  };

  filtroUsuarioId = signal<number | ''>('')

  fichajeEditando = signal<IFichajes | null>(null)
  modalAbierto = signal(false)

  fichajeFiltrados = computed(() => {
    const id = this.filtroUsuarioId()
    if (!id) return this.fichajes()
    return this.fichajes().filter(f => f.usuario?.id === id)
  })

  async ngOnInit() {
    await this.cargarDatos()
  }

  async cargarDatos() {
    this.cargando.set(true)
    try {
      const [fichajesRes, usuariosRes] = await Promise.all([
        this.fichajeService.getFichajes(),
        this.fichajeService.getUsuarios()
      ])
      this.usuarios.set(usuariosRes)
      const datosBrutos = fichajesRes.content ?? fichajesRes

      const fichajesVinculados = datosBrutos.map((f: any) => {
        const usuarioEncontrado = usuariosRes.find((u: any) => u.id === f.usuarioId || u.id === f.usuario?.id);
        if (!usuarioEncontrado) {
          console.warn('No se encontró usuario para fichaje', f);
        }
        return {
          ...f,
          usuario: usuarioEncontrado
        }
      })

      this.fichajes.set(fichajesVinculados);

    } catch (e) {
      this.error.set('Error al cargar los datos');
      console.error(e);
    } finally {
      this.cargando.set(false);
    }
  }

  abrirEdicion(f: IFichajes) {
    this.fichajeEditando.set({ ...f })
    this.modalAbierto.set(true)
  }

  cerrarModal() {
    this.modalAbierto.set(false)
    this.fichajeEditando.set(null)
  }

  async guardarEdicion() {
    const f = this.fichajeEditando()
    if (!f) return
    try {
      await this.fichajeService.updateFichaje(f.id, f)
      await this.cargarDatos()
      this.cerrarModal()
    } catch (e) {
      this.error.set('Error al actualizar el fichaje')
    }
  }

  async eliminar(id: number) {
    if (!confirm('¿Seguro que quieres eliminar este fichaje?')) return
    try {
      await this.fichajeService.deleteFichaje(id)
      this.fichajes.update(list => list.filter(f => f.id !== id))
    } catch (e) {
      this.error.set('Error al eliminar el fichaje')
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
