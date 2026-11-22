export interface ComentarioResponse {
  id: number,
  contenido: string,
  autorId: number,
  nombreAutor: string,
  consultaId: number,
  numeroConsulta: string,
  fechaCreacion: string | Date,
  editado: boolean
}