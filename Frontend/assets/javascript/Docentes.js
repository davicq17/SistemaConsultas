// =============================
// 🌐 URLs del proyecto
// =============================
const API_URL = "http://localhost:8080";

// =============================
// 🔒 Seguridad de acceso y Sesión
// =============================
const idDocente = localStorage.getItem("id_usuario");
const rol = localStorage.getItem("rol");
const token = localStorage.getItem("token");
let nombreUsuario = localStorage.getItem("nombre_usuario");
// Si ya guardamos la identificación antes, la leemos, si no, usamos el id_usuario temporalmente
let identificacionDocente = localStorage.getItem("identificacion_docente") || idDocente;

const nombreDiv = document.getElementById("nombreUsuarioHeader");

// Validación de sesión para evitar peticiones con "null"
if (!idDocente || !token) {
    alert("⚠️ No se encontró una sesión activa. Por favor, inicie sesión nuevamente.");
    cerrarSesion();
} else {
    if (nombreUsuario && localStorage.getItem("identificacion_docente")) {
        if (nombreDiv) nombreDiv.textContent = `Hola, ${nombreUsuario}`;
    } else {
        // Intentamos obtener los datos del usuario usando el endpoint general de usuarios de tu BD
        fetchAPI(`/usuarios/${idDocente}`) // Cambia a /usuarios o /docentes según tu backend si es necesario
            .then(res => res.json())
            .then(data => {
                const usuarioData = data.usuario || data.docente || data;
                if (usuarioData) {
                    const nombre = usuarioData.nombre || data.nombre;
                    // Guardamos la identificación real (ej: "12345") en el localStorage
                    const identificacion = usuarioData.identificacion || usuarioData.cedula || usuarioData.documento || "12345";

                    localStorage.setItem("nombre_usuario", nombre);
                    localStorage.setItem("identificacion_docente", identificacion);
                    identificacionDocente = identificacion;

                    if (nombreDiv) nombreDiv.textContent = `Hola, ${nombre}`;
                }
            })
            .catch(err => {
                console.warn("⚠️ No se pudo obtener la cédula mediante el endpoint, usando ID por defecto.");
            });
    }
}

// =============================
// 🛠️ Función Helper para Peticiones HTTP
// =============================
function fetchAPI(endpoint, options = {}) {
    const headers = {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
        ...options.headers
    };

    return fetch(`${API_URL}${endpoint}`, { ...options, headers })
        .then(res => {
            if (res.status === 401 || res.status === 403) {
                alert("Tu sesión ha expirado o no tienes permisos.");
                cerrarSesion();
                throw new Error("No autorizado");
            }
            return res;
        });
}

// =============================
// Variables Globales
// =============================
let todasLasSolicitudes = [];

// =============================
// 🚀 INICIALIZACIÓN AL CARGAR LA PÁGINA
// =============================
document.addEventListener("DOMContentLoaded", () => {
    // Usamos la identificación almacenada o el ID por defecto
    const cedulaActual = localStorage.getItem("identificacion_docente") || identificacionDocente || idDocente;
    if (cedulaActual) {
        obtener_solicitudes_docente(cedulaActual);
    }
    cargarmodulos();
    cargarLugarConsultaUnificado();
    registrarSolicitud();
});

// =============================
// 📚 CARGAR MÓDULOS
// =============================
async function cargarmodulos() {
    try {
        const response = await fetchAPI(`/modulos`);
        if (!response || !response.ok) return;

        const data = await response.json();
        const select = document.getElementById("buscar_modulo") || document.getElementById("idModuloSelect");
        if (!select) return;
        select.innerHTML = '<option value="">Seleccione un módulo</option>';

        const listaModulos = Array.isArray(data) ? data : (data.content || data.modulos || []);
        listaModulos.forEach(p => {
            const option = document.createElement("option");
            option.value = p.id || p.codigo;
            option.textContent = `${p.id || p.codigo} - ${p.nombre}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error("Error al cargar módulos:", error);
    }
}

// =============================
// 📅 REGISTRAR SOLICITUD
// =============================
function registrarSolicitud() {
    const form = document.getElementById("consultaForm");
    if (!form) return;

    form.addEventListener("submit", async e => {
        e.preventDefault();

        const datos = {
            docenteId: Number(idDocente),
            estudianteId: Number(document.getElementById("numeroDocumentoEstudiante")?.value.trim() || 0),
            moduloId: Number(document.getElementById("buscar_modulo")?.value || 0),
            asunto: document.getElementById("temaConsulta")?.value.trim() || "",
            descripcion: document.getElementById("descripcionConsulta")?.value.trim() || "",
            fechaConsulta: document.getElementById("fechaConsulta")?.value || "",
            horaConsulta: document.getElementById("horaConsulta")?.value || "",
            prioridad: document.getElementById("prioridad")?.value || "MEDIA",
            recursoFisicoId: Number(document.getElementById("Lugar_consulta")?.value || 0),
            numeroConsulta: "C-" + Date.now()
        };

        try {
            const res = await fetchAPI(`/solicitudes-consultas`, {
                method: "POST",
                body: JSON.stringify(datos)
            });

            const data = await res.json().catch(() => ({}));

            if (res.ok) {
                alert("✅ Solicitud registrada exitosamente.");
                form.reset();
                const cedulaActual = localStorage.getItem("identificacion_docente") || idDocente;
                obtener_solicitudes_docente(cedulaActual);
            } else {
                alert(data.message || "Error al registrar la solicitud.");
            }
        } catch (err) {
            console.error("Error al registrar:", err);
            alert("Error de conexión con el servidor.");
        }
    });
}

// =============================
// 📨 OBTENER SOLICITUDES DEL DOCENTE
// =============================
// =============================
// 📨 OBTENER SOLICITUDES DEL DOCENTE
// =============================
async function obtener_solicitudes_docente(targetIdDocente) {
    try {
        let identificacionReal = localStorage.getItem("identificacion_docente");

        // Si no la tenemos guardada, la buscamos consultando todos los usuarios
        if (!identificacionReal || identificacionReal === String(targetIdDocente)) {
            const resUsuarios = await fetchAPI('/usuarios');
            if (resUsuarios && resUsuarios.ok) {
                const dataUsrs = await resUsuarios.json();
                const listaUsrs = Array.isArray(dataUsrs) ? dataUsrs : (dataUsrs.usuarios || dataUsrs.content || []);

                // Buscamos el usuario cuyo ID coincida con el de sesión (ej: id = 5)
                const usuarioActual = listaUsrs.find(u => String(u.id || u.idUsuario) === String(targetIdDocente));

                if (usuarioActual) {
                    identificacionReal = String(usuarioActual.identificacion || usuarioActual.cedula || "").trim();
                    localStorage.setItem("identificacion_docente", identificacionReal);
                }
            }
        }

        if (!identificacionReal) {
            identificacionReal = String(targetIdDocente);
        }

        console.log("🔍 ID de Sesión:", targetIdDocente, "-> Cédula real encontrada:", identificacionReal);

        // Traemos todas las solicitudes del sistema
        const res = await fetchAPI(`/solicitudes-consultas`);
        if (!res || !res.ok) return;

        const data = await res.json();
        const listaGeneral = Array.isArray(data) ? data : (data.content || data.solicitudes || data.consultas || []);

        // Filtramos comparando la cédula real ("12345") contra la solicitud
        todasLasSolicitudes = listaGeneral.filter(s => {
            const idEnRegistro = String(s.identificacionDocente || "").trim();
            return idEnRegistro === identificacionReal;
        });

        console.log("✅ Solicitudes que pasaron el filtro:", todasLasSolicitudes);
        actualizarTablaSolicitudes(todasLasSolicitudes);

    } catch (err) {
        console.error("Error al obtener solicitudes:", err);
        todasLasSolicitudes = [];
        actualizarTablaSolicitudes([]);
    }
}

// =============================
// 📊 TABLA DE SOLICITUDES
// =============================
// =============================
// 📊 TABLA DE SOLICITUDES (Con nombres y datos completos)
// =============================
function actualizarTablaSolicitudes(solicitudes) {
    const tbody = document.querySelector("#tablasolicitudes tbody") ||
        document.querySelector("#tablasolicitudes_consultas tbody") ||
        document.querySelector("#tablaconsultas tbody");

    if (!tbody) return;

    tbody.innerHTML = "";

    if (!solicitudes || solicitudes.length === 0) {
        const fila = tbody.insertRow();
        const celda = fila.insertCell(0);
        celda.colSpan = 9; // Actualizado al nuevo total de columnas
        celda.textContent = "✅ No hay solicitudes pendientes.";
        celda.style.textAlign = "center";
        return;
    }

    solicitudes.forEach(s => {
        const fila = tbody.insertRow();

        // 1. ID interno
        fila.insertCell(0).textContent = s.id || "—";

        // 2. Número de consulta (ej: SC-2026-000002)
        fila.insertCell(1).textContent = s.numeroConsulta || s.numero_consulta || "—";

        // 3. Estudiante (Nombre completo o ID si no viene el objeto)
        fila.insertCell(2).textContent = s.nombreCompletoEstudiante || s.estudianteNombre || `ID: ${s.estudianteId || s.estudiante_id || "—"}`;

        // 4. Módulo (Nombre del módulo o ID)
        fila.insertCell(3).textContent = s.nombreModulo || s.moduloNombre || `Módulo ${s.moduloId || s.modulo_id || "—"}`;

        // 5. Tema / Asunto
        fila.insertCell(4).textContent = s.asunto || s.tema || "—";

        // 6. Fecha y Hora unidas para optimizar espacio
        const fecha = s.fechaConsulta || s.fecha || "";
        const hora = s.horaConsulta || s.hora || "";
        fila.insertCell(5).textContent = `${fecha} ${hora}`.trim() || "—";

        // 7. Prioridad
        fila.insertCell(6).textContent = s.prioridad || "MEDIA";

        // 8. Estado
        fila.insertCell(7).textContent = s.estado || "Pendiente";

        // 9. Acciones (Aceptar / Rechazar)
        const celdaAcciones = fila.insertCell(8);
        const btnAceptar = document.createElement("button");
        btnAceptar.textContent = "✅ Aceptar";
        btnAceptar.className = "btn-aceptar";
        btnAceptar.onclick = () => responderSolicitud(s.id, "ACEPTADA");

        const btnRechazar = document.createElement("button");
        btnRechazar.textContent = "❌ Rechazar";
        btnRechazar.className = "btn-rechazar";
        btnRechazar.onclick = () => responderSolicitud(s.id, "RECHAZADA");

        celdaAcciones.appendChild(btnAceptar);
        celdaAcciones.appendChild(btnRechazar);
    });
}

// =============================
// ⚙️ RESPONDER SOLICITUD
// =============================
async function responderSolicitud(id, estado) {
    try {
        const res = await fetchAPI(`/solicitudes-consultas/${id}/estado`, {
            method: "PATCH",
            body: JSON.stringify({ estado })
        });

        if (res.ok) {
            alert(`Solicitud ${estado.toLowerCase()} con éxito.`);
            const cedulaActual = localStorage.getItem("identificacion_docente") || idDocente;
            obtener_solicitudes_docente(cedulaActual);
        } else {
            alert("No se pudo actualizar el estado de la solicitud.");
        }
    } catch (err) {
        console.error("Error al responder solicitud:", err);
    }
}

// =============================
// CARGAR RECURSO FÍSICO
// =============================
async function cargarLugarConsultaUnificado() {
    const selectLugar = document.getElementById("Lugar_consulta");
    if (!selectLugar) return;

    try {
        const [resSedes, resBloques, resRecursos] = await Promise.all([
            fetchAPI('/sedes'),
            fetchAPI('/bloques'),
            fetchAPI('/recursos-fisicos')
        ]);

        if (!resSedes || !resBloques || !resRecursos) return;

        const dataSedes = await resSedes.json();
        const dataBloques = await resBloques.json();
        const dataRecursos = await resRecursos.json();

        const listaSedes = Array.isArray(dataSedes) ? dataSedes : (dataSedes.sedes || dataSedes.content || []);
        const listaBloques = Array.isArray(dataBloques) ? dataBloques : (dataBloques.bloques || dataBloques.content || []);
        const listaRecursos = Array.isArray(dataRecursos) ? dataRecursos : (dataRecursos.recursos || dataRecursos.content || []);

        const sedesMap = {};
        listaSedes.forEach(s => {
            sedesMap[s.id || s.idSede] = s.nombre || s.nombreSede;
        });

        const bloquesMap = {};
        listaBloques.forEach(b => {
            const sId = b.sedeId || b.sede?.id || b.idSede;
            bloquesMap[b.id || b.idBloque] = {
                nombre: b.nombre || b.nombreBloque,
                sedeId: sId
            };
        });

        selectLugar.innerHTML = '<option value="">Seleccione un recurso físico</option>';

        if (listaRecursos.length === 0) {
            selectLugar.innerHTML = '<option value="">No hay recursos registrados</option>';
            return;
        }

        listaRecursos.forEach(r => {
            const recursoId = r.id || r.idRecurso;
            const nombreRecurso = r.nombre || r.nombreRecurso || "Sin nombre";
            const tipoRecurso = r.tipo ? `(${r.tipo})` : "";

            const bId = r.bloqueId || r.bloque?.id || r.idBloque;
            const bloqueInfo = bloquesMap[bId] || { nombre: "Bloque Desconocido", sedeId: null };
            const nombreSede = sedesMap[bloqueInfo.sedeId] || "Sede Desconocida";

            const textoCompleto = `${nombreSede} / ${bloqueInfo.nombre} / ${nombreRecurso} ${tipoRecurso}`;

            const opt = document.createElement("option");
            opt.value = recursoId;
            opt.textContent = textoCompleto;
            selectLugar.appendChild(opt);
        });

    } catch (error) {
        console.error("Error al cargar la jerarquía de recursos para la consulta:", error);
        selectLugar.innerHTML = '<option value="">Error al cargar los lugares</option>';
    }
}

// =============================
// 🛠️ Utilidades de UI y Sesión
// =============================
function openTab(evt, tabName) {
    document.querySelectorAll(".tabcontent").forEach(tab => tab.style.display = "none");
    document.querySelectorAll(".tablink").forEach(btn => btn.classList.remove("active"));

    const target = document.getElementById(tabName);
    if (target) target.style.display = "block";
    if (evt) evt.currentTarget.classList.add("active");
}

function cerrarSesion() {
    localStorage.clear();
    window.location.href = "index.html";
}

function exportarformato() {
    window.location.href = "formato.html";
}