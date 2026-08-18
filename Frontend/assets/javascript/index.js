document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();
    loginUsuario();
});

const API_URL = "http://localhost:8080";

async function loginUsuario() {
    const correo = document.getElementById('correo').value.trim();
    const password = document.getElementById('password').value.trim();
    const mensajeLogin = document.getElementById('error-message');

    // Limpiar mensaje anterior
    mensajeLogin.innerText = '';

    try {
        const response = await fetch(`${API_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ correo, password })
        });

        // Intentar parsear el JSON independientemente del código de estado HTTP
        const data = await response.json();

        if (!response.ok) {
            // Manejar errores HTTP (401, 403, 400, etc.)
            mensajeLogin.innerText = data.message || 'Usuario o contraseña incorrectos.';
            return;
        }

        // Guardar sesión si el login fue exitoso
        if (data.token) {
            // 🔑 CORRECCIÓN AQUÍ:
            localStorage.setItem('correo', correo);
            localStorage.setItem('token', data.token);
            localStorage.setItem('id_usuario', data.id_usuario);

            if (data.rol) localStorage.setItem('rol', data.rol);
            if (data.nombre) {
                localStorage.setItem('nombre', data.nombre);
                localStorage.setItem('nombre_usuario', data.nombre);
            }

            // Redirección según rol
            switch (data.rol) {
                case 'ADMINISTRADOR':
                    window.location.href = 'Administrador.html';
                    break;
                case 'DOCENTE':
                    window.location.href = 'docente.html';
                    break;
                case 'ESTUDIANTE':
                    window.location.href = 'estudiante.html';
                    break;
                default:
                    window.location.href = 'index.html';
            }
        } else {
            mensajeLogin.innerText = 'Respuesta inesperada del servidor.';
        }

    } catch (error) {
        console.error('Error al iniciar sesión:', error);
        mensajeLogin.innerText = 'No se pudo conectar con el servidor. Revisa tu conexión.';
    }
}