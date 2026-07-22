# Flujo de Trabajo con Git

## Objetivo

Mantener un historial limpio, evitar cambios directos sobre la rama principal y garantizar que todo el código sea revisado antes de integrarse.

---

# Ramas del proyecto

## main

Contiene únicamente versiones estables del proyecto.

Nunca se desarrollan funcionalidades directamente sobre esta rama.

---

## develop

Es la rama principal de desarrollo.

Todas las nuevas funcionalidades deben integrarse primero aquí.

---

# Flujo de trabajo

1. Actualizar la rama develop.

2. Crear una rama para la nueva funcionalidad.

Ejemplo:

feature/usuarios

feature/programas

feature/modulos

feature/consultas

3. Desarrollar la funcionalidad.

4. Realizar commits descriptivos.

5. Hacer Push al repositorio.

6. Crear Pull Request hacia develop.

7. Revisar el código.

8. Aprobar el Pull Request.

9. Realizar Merge.

10. Cuando develop sea estable, crear Pull Request hacia main.

---

# Reglas

- No hacer Push directo a main.
- Todos los cambios deben pasar por Pull Request.
- Cada funcionalidad debe desarrollarse en su propia rama.
- No mezclar varias funcionalidades en una misma rama.

---

# Ejemplo

main

↑

Pull Request

↑

develop

↑

Pull Request

↑

feature/usuarios
