document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();
    loginUsuario();
});

///////////////////////
// Aquí va la dirección de la API
const API_URL = "http://localhost:8080";
///////////////////////

function loginUsuario() {
    const correo = document.getElementById('correo').value.trim();
    const password = document.getElementById('password').value.trim();
    const mensajeLogin = document.getElementById('error-message');

    // 👈 CAMBIO AQUÍ: Se agrega '/auth/login' para coincidir con tu SecurityConfig.kt
    fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ correo, password })
    })
        .then(response => {
            // Manejo de respuestas con código de error HTTP (como 401, 403, 500)
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.success || data.token) { // Verifica la propiedad que devuelva tu backend (token o success)
                localStorage.setItem('correo', correo);
                localStorage.setItem('rol', data.rol);
                localStorage.setItem('nombre', data.nombre);

                // Guarda el token JWT si tu backend lo retorna
                if (data.token) {
                    localStorage.setItem('token', data.token);
                }

                if (data.rol === 'ADMINISTRADOR') {
                    window.location.href = 'administrador.html';
                } else if (data.rol === 'DOCENTE') {
                    window.location.href = 'docente.html';
                } else if (data.rol === 'ESTUDIANTE') {
                    window.location.href = 'estudiante.html';
                }
            } else {
                mensajeLogin.innerText = 'Usuario o contraseña incorrectos.';
            }
        })
        .catch(error => {
            console.error('Error al iniciar sesión:', error);
            mensajeLogin.innerText = 'Usuario o contraseña incorrectos o error en el servidor.';
        });
}