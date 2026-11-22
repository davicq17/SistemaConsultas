import { Rol } from '../enums/rol.enum';

export interface LoginResponse {
  token: string;
  id_usuario: number;
  correo: string;
  nombre: string;
  rol: Rol;
  tipo: string;
}
