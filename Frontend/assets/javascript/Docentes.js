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
        fetchAPI(`/usuarios/${idDocente}`)
            .then(res => res.json())
            .then(data => {
                const usuarioData = data.usuario || data.docente || data;
                if (usuarioData) {
                    const nombre = usuarioData.nombre || data.nombre;
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
async function obtener_solicitudes_docente(targetIdDocente) {
    try {
        let identificacionReal = localStorage.getItem("identificacion_docente");

        if (!identificacionReal || identificacionReal === String(targetIdDocente)) {
            const resUsuarios = await fetchAPI('/usuarios');
            if (resUsuarios && resUsuarios.ok) {
                const dataUsrs = await resUsuarios.json();
                const listaUsrs = Array.isArray(dataUsrs) ? dataUsrs : (dataUsrs.usuarios || dataUsrs.content || []);

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

        const res = await fetchAPI(`/solicitudes-consultas`);
        if (!res || !res.ok) return;

        const data = await res.json();
        const listaGeneral = Array.isArray(data) ? data : (data.content || data.solicitudes || data.consultas || []);

        todasLasSolicitudes = listaGeneral.filter(s => {
            const idEnRegistro = String(s.identificacionDocente || s.docenteId || "").trim();
            return idEnRegistro === identificacionReal || idEnRegistro === String(targetIdDocente);
        });

        actualizarTablaSolicitudes(todasLasSolicitudes);

    } catch (err) {
        console.error("Error al obtener solicitudes:", err);
        todasLasSolicitudes = [];
        actualizarTablaSolicitudes([]);
    }
}

// =============================
// 🔍 FILTRO DE CONSULTAS / SOLICITUDES (FUNCIÓN AÑADIDA)
// =============================
function obtenerConsultasFiltradas() {
    const fecha = document.getElementById("buscarFecha")?.value;
    const hora = document.getElementById("buscarHora")?.value;
    const mes = document.getElementById("buscarMes")?.value;
    const idModuloFiltro = document.getElementById("buscar_modulo")?.value || document.getElementById("idModuloSelect")?.value;

    let filtradas = [...todasLasSolicitudes];

    if (fecha) {
        filtradas = filtradas.filter(c => (c.fechaConsulta || c.fecha) === fecha);
    }
    if (hora) {
        filtradas = filtradas.filter(c => (c.horaConsulta || c.hora) === hora);
    }
    if (mes) {
        filtradas = filtradas.filter(c => {
            const f = c.fechaConsulta || c.fecha;
            if (!f) return false;
            const mesFecha = new Date(f.includes('T') ? f : f + 'T00:00:00').getMonth() + 1;
            return String(mesFecha) === String(mes);
        });
    }
    if (idModuloFiltro) {
        filtradas = filtradas.filter(c => String(c.moduloId || c.modulo_id) === String(idModuloFiltro));
    }

    actualizarTablaSolicitudes(filtradas);
}

// =============================
// 📊 TABLA DE SOLICITUDES
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
        celda.colSpan = 9;
        celda.textContent = "✅ No hay solicitudes o consultas registradas.";
        celda.style.textAlign = "center";
        return;
    }

    solicitudes.forEach(s => {
        const fila = tbody.insertRow();

        // 1. ID interno
        fila.insertCell(0).textContent = s.id || "—";

        // 2. Número de consulta
        fila.insertCell(1).textContent = s.numeroConsulta || s.numero_consulta || "—";

        // 3. Estudiante
        fila.insertCell(2).textContent = s.nombreCompletoEstudiante || s.estudianteNombre || `ID: ${s.estudianteId || s.estudiante_id || "—"}`;

        // 4. Módulo
        fila.insertCell(3).textContent = s.nombreModulo || s.moduloNombre || `Módulo ${s.moduloId || s.modulo_id || "—"}`;

        // 5. Tema / Asunto
        fila.insertCell(4).textContent = s.asunto || s.tema || "—";

        // 6. Fecha y Hora
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