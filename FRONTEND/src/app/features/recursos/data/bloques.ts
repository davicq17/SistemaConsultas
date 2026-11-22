export interface Bloque {
  id: number;
  nombre: string;
}

export const BLOQUES: readonly Bloque[] = [
  { id: 1, nombre: 'Bloque A' },
  { id: 2, nombre: 'Bloque B' },
  { id: 3, nombre: 'Bloque C' },
];

export function nombreBloque(id: number): string {
  return BLOQUES.find((bloque) => bloque.id === id)?.nombre ?? 'Sin bloque asignado';
}
