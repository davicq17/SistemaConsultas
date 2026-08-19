document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById('registroForm');

    form.addEventListener('submit', function (e) {
        e.preventDefault();

        // Obtener valores del formulario
        let nombre = document.getElementById('nombre').value.trim();
        let numeroDoc = document.getElementById('numeroDocumento').value.trim();
        let apellido = document.getElementById('apellido').value.trim();
        let correo = document.getElementById('correo').value.trim();
        //let id_programa = document.getElementById('programaAcademico').value;//
        let contra = document.getElementById('contra').value;

        // Eliminar espacios en la contraseña
        contra = contra.replace(/\s+/g, "");

        // Validaciones
        if (!nombre || !numeroDoc || !contra) {
            alert("⚠️ Todos los campos son obligatorios");
            return;
        }

        // Corrección de Regex: Cambiado de {8,50} a {3,50}
        const regexNombre = /^[A-Za-zÁÉÍÓÚÑáéíóúñ\s]{3,50}$/;
        if (!regexNombre.test(nombre)) {
            alert("⚠️ El nombre solo puede contener letras y debe tener entre 3 y 50 caracteres");
            return;
        }

        const regexDoc = /^[0-9]{6,12}$/;
        if (!regexDoc.test(numeroDoc)) {
            alert("⚠️ El número de documento debe contener entre 6 y 12 dígitos");
            return;
        }

        const regexContra = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_\-+=<>?{}[\]~]).{8,}$/;
        if (!regexContra.test(contra)) {
            alert("⚠️ La contraseña debe tener:\n- Mínimo 8 caracteres\n- Una mayúscula\n- Una minúscula\n- Un número\n- Un carácter especial");
            return;
        }

        let datos = {
            identificacion: numeroDoc,
            nombre: nombre,
            apellido:apellido,
            rol: "ESTUDIANTE",
            apellido: apellido,
            correo: correo,
            password: contra
        };

        // Enviar datos al backend
        fetch(`${API_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        })
            .then(async response => {
                if (response.ok) {
                    alert('✅ Estudiante registrado exitosamente');
                    form.reset();
                } else {
                    const errorData = await response.json().catch(() => ({}));
                    alert('⚠️ ' + (errorData.message || 'El estudiante ya está registrado o datos inválidos'));
                }
            })
            .catch(error => {
                console.error('❌ Error al enviar la solicitud:', error);
                alert('⚠️ Error de conexión con el servidor');
            });
    });
});

const API_URL = "http://localhost:8080";

// =============================
// Cargar Programas Académicos
// =============================
function cargarProgramas() {
    // Endpoint alineado con el controller: GET /programas
    fetch(`${API_URL}/programas`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error en la petición: ${response.status}`);
            }
            return response.json(); // Devuelve una lista/array directamente
        })
        .then(programas => {
            const select = document.getElementById('programaAcademico');
            select.innerHTML = '<option value="">Seleccione un programa</option>';

            programas.forEach(p => {
                const option = document.createElement('option');
                option.value = p.id;
                // Asumiendo que el DTO ProgAcademicoResponse mapea la propiedad 'nombre'
                option.textContent = p.nombre;
                select.appendChild(option);
            });
        })
        .catch(error => console.error('❌ Error al cargar programas:', error));
}

cargarProgramas();