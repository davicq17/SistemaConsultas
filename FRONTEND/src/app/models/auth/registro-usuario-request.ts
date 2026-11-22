import { Rol } from '../enums/rol.enum';

export interface RegistroUsuarioRequest {
  identificacion: string;
  nombre: string;
  apellido: string;
  correo: string;
  password: string;
  rol: Rol;
  programaId?: number;
}
