// =============================
// 🌐 URLs del proyecto
// =============================
const API_URL = "http://localhost:8080";

// =============================
// 🔒 Seguridad de acceso y Sesión
// =============================
const idEstudiante = localStorage.getItem("id_usuario");
const rol = localStorage.getItem("rol");
const token = localStorage.getItem("token");
let nombreUsuario = localStorage.getItem("nombre_usuario");
let identificacionEstudiante = localStorage.getItem("identificacion_estudiante") || idEstudiante;

const nombreDiv = document.getElementById("nombreUsuario");

if (!idEstudiante || !token) {
    alert("⚠️ No se encontró una sesión activa. Por favor, inicie sesión nuevamente.");
    cerrarSesion();
} else if (rol !== "ESTUDIANTE") {
    alert("⚠️ No tienes permisos para acceder a esta sección.");
    cerrarSesion();
} else {
    if (nombreUsuario && localStorage.getItem("identificacion_estudiante")) {
        if (nombreDiv) nombreDiv.textContent = `Hola, ${nombreUsuario}`;
    } else {
        fetchAPI(`/usuarios/${idEstudiante}`)
            .then(res => res.json())
            .then(data => {
                const usuarioData = data.usuario || data.estudiante || data;
                if (usuarioData) {
                    const nombre = usuarioData.nombre || data.nombre;
                    const identificacion = usuarioData.identificacion || usuarioData.cedula || usuarioData.documento || idEstudiante;

                    localStorage.setItem("nombre_usuario", nombre);
                    localStorage.setItem("identificacion_estudiante", identificacion);
                    identificacionEstudiante = identificacion;

                    if (nombreDiv) nombreDiv.textContent = `Hola, ${nombre}`;
                }
            })
            .catch(() => {
                console.warn("⚠️ No se pudo obtener la cédula mediante el endpoint de usuario.");
            });
    }
}

// =============================
// 🛠️ Helper Peticiones HTTP
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
let consultas = [];
let todasLasSolicitudes = [];
let calendarInstance = null;

// =============================
// 🚀 INICIALIZACIÓN
// =============================
document.addEventListener("DOMContentLoaded", () => {
    const cedulaActual = localStorage.getItem("identificacion_estudiante") || identificacionEstudiante || idEstudiante;

    if (cedulaActual) {
        obtener_consultas_estudiante(cedulaActual);
        obtener_solicitudes_estudiante(cedulaActual);
    }

    cargarModulos();
    cargarDocentes();
    cargarLugarConsulta();
    registrarSolicitudConsulta();
});

// =============================
// 📚 CARGAR MÓDULOS
// =============================
async function cargarModulos() {
    try {
        const response = await fetchAPI(`/modulos`);
        if (!response || !response.ok) return;

        const data = await response.json();
        const select = document.getElementById("modulo");
        if (!select) return;

        select.innerHTML = '<option value="">Seleccione un módulo</option>';
        const listaModulos = Array.isArray(data) ? data : (data.content || data.modulos || []);

        listaModulos.forEach(m => {
            const option = document.createElement("option");
            option.value = m.id || m.codigo;
            option.textContent = `${m.id || m.codigo} - ${m.nombre}`;
            select.appendChild(option);
        });
    } catch (error) {
        console.error("Error al cargar módulos:", error);
    }
}

// =============================
// 👨‍🏫 CARGAR DOCENTES
// =============================
async function cargarDocentes() {
    try {
        const response = await fetchAPI(`/usuarios`);
        if (!response || !response.ok) return;

        const data = await response.json();
        const selectSolicitud = document.getElementById("docente");
        const selectFiltro = document.getElementById("buscarDocente");
        const selectDocenteHorario = document.getElementById("selectDocenteHorario");

        const listaUsuarios = Array.isArray(data) ? data : (data.content || data.usuarios || []);
        const listaDocentes = listaUsuarios.filter(u => String(u.rol || u.tipoUsuario).toUpperCase() === "DOCENTE");

        const cargarEnSelect = (selectElement, textoPorDefecto) => {
            if (!selectElement) return;
            selectElement.innerHTML = `<option value="">${textoPorDefecto}</option>`;
            listaDocentes.forEach(d => {
                const opt = document.createElement("option");
                opt.value = d.id || d.idUsuario;
                opt.textContent = d.nombre;
                selectElement.appendChild(opt);
            });
        };

        cargarEnSelect(selectSolicitud, "Seleccione un docente");
        cargarEnSelect(selectFiltro, "Todos los docentes");
        cargarEnSelect(selectDocenteHorario, "Seleccione un docente");
    } catch (error) {
        console.error("Error al cargar docentes:", error);
    }
}

// =============================
// 📅 CONSULTAR DISPONIBILIDAD EN CALENDARIO
// =============================
async function obtenerHorarioAtencionDocente() {
    const idDocenteSel = document.getElementById("selectDocenteHorario")?.value;
    const calendarEl = document.getElementById("calendarioDocente");

    if (!calendarEl) return;

    if (calendarInstance) {
        calendarInstance.destroy();
        calendarInstance = null;
    }

    if (!idDocenteSel) {
        calendarEl.innerHTML = '<p style="text-align:center; padding: 20px;">⚠️ Seleccione un docente para cargar su agenda en el calendario.</p>';
        return;
    }

    try {
        const resSolicitudes = await fetchAPI(`/solicitudes-consultas`).catch(() => null);

        let eventos = [];

        if (resSolicitudes && resSolicitudes.ok) {
            const dataSol = await resSolicitudes.json();
            const listaGeneral = Array.isArray(dataSol) ? dataSol : (dataSol.content || dataSol.solicitudes || []);

            const ocupadas = listaGeneral.filter(s =>
                String(s.docenteId) === String(idDocenteSel) &&
                ["ACEPTADA", "AGENDADA", "REALIZADA", "PENDIENTE"].includes(String(s.estado).toUpperCase())
            );

            eventos = ocupadas.map(s => {
                const fecha = s.fechaConsulta || s.fecha || "";
                const hora = s.horaConsulta || s.hora || "00:00:00";
                const startIso = fecha ? `${fecha}T${hora}` : null;

                let color = "#17a2b8"; // PENDIENTE (Azul)
                const estadoUpper = String(s.estado).toUpperCase();
                if (estadoUpper === "ACEPTADA" || estadoUpper === "AGENDADA") color = "#dc3545"; // ACEPTADA/AGENDADA (Rojo)
                else if (estadoUpper === "REALIZADA") color = "#6c757d"; // REALIZADA (Gris)

                return {
                    id: s.id,
                    title: `${s.nombreModulo || 'Módulo ' + (s.moduloId || '')} - (${s.estado})`,
                    start: startIso,
                    backgroundColor: color,
                    borderColor: color,
                    allDay: !hora || hora === "00:00:00"
                };
            }).filter(e => e.start);
        }

        calendarInstance = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'es',
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek,timeGridDay'
            },
            buttonText: {
                today: 'Hoy',
                month: 'Mes',
                week: 'Semana',
                day: 'Día'
            },
            events: eventos,
            eventClick: function(info) {
                alert(`📌 Consulta: ${info.event.title}\n📅 Inicio: ${info.event.start ? info.event.start.toLocaleString() : 'N/A'}`);
            }
        });

        calendarInstance.render();

    } catch (error) {
        console.error("Error al obtener consultas del docente para el calendario:", error);
        calendarEl.innerHTML = '<p style="text-align:center; color: red;">Error al cargar el calendario del docente.</p>';
    }
}

// =============================
// 🏢 CARGAR RECURSOS FÍSICOS
// =============================
async function cargarLugarConsulta() {
    const selectLugar = document.getElementById("lugar");
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
        listaSedes.forEach(s => { sedesMap[s.id || s.idSede] = s.nombre || s.nombreSede; });

        const bloquesMap = {};
        listaBloques.forEach(b => {
            const sId = b.sedeId || b.sede?.id || b.idSede;
            bloquesMap[b.id || b.idBloque] = { nombre: b.nombre || b.nombreBloque, sedeId: sId };
        });

        selectLugar.innerHTML = '<option value="">Seleccione un recurso físico</option>';

        listaRecursos.forEach(r => {
            const recursoId = r.id || r.idRecurso;
            const nombreRecurso = r.nombre || r.nombreRecurso || "Sin nombre";
            const bId = r.bloqueId || r.bloque?.id || r.idBloque;
            const bloqueInfo = bloquesMap[bId] || { nombre: "Bloque Desconocido", sedeId: null };
            const nombreSede = sedesMap[bloqueInfo.sedeId] || "Sede Desconocida";

            const opt = document.createElement("option");
            opt.value = recursoId;
            opt.textContent = `${nombreSede} / ${bloqueInfo.nombre} / ${nombreRecurso}`;
            selectLugar.appendChild(opt);
        });
    } catch (error) {
        console.error("Error al cargar lugares de consulta:", error);
    }
}

// =============================
// 📅 REGISTRAR SOLICITUD
// =============================
function registrarSolicitudConsulta() {
    const form = document.getElementById("formSolicitud");
    if (!form) return;

    form.addEventListener("submit", async e => {
        e.preventDefault();

        const temaTexto = document.getElementById("tema")?.value.trim() || "";

        let horaInput = document.getElementById("hora")?.value || "";
        if (horaInput && horaInput.split(":").length === 2) {
            horaInput += ":00";
        }

        const datos = {
            estudianteId: Number(idEstudiante),
            docenteId: Number(document.getElementById("docente")?.value || 0),
            moduloId: Number(document.getElementById("modulo")?.value || 0),
            asunto: temaTexto,
            descripcion: document.getElementById("descripcion")?.value.trim() || temaTexto || "Solicitud de consulta académica",
            fechaConsulta: document.getElementById("fecha")?.value || "",
            horaConsulta: horaInput,
            recursoFisicoId: Number(document.getElementById("lugar")?.value || 0),
            prioridad: document.getElementById("prioridad")?.value || "MEDIA",
            numeroConsulta: "SC-" + Date.now()
        };

        if (!datos.docenteId || !datos.moduloId) {
            alert("⚠️ Debes seleccionar un módulo y un docente.");
            return;
        }

        try {
            const res = await fetchAPI(`/solicitudes-consultas`, {
                method: "POST",
                body: JSON.stringify(datos)
            });

            const data = await res.json().catch(() => ({}));

            if (res.ok) {
                alert("✅ Solicitud enviada exitosamente.");
                form.reset();
                const cedulaActual = localStorage.getItem("identificacion_estudiante") || idEstudiante;
                obtener_consultas_estudiante(cedulaActual);
                obtener_solicitudes_estudiante(cedulaActual);
            } else {
                alert(data.message || "Error al enviar la solicitud.");
            }
        } catch (err) {
            console.error("Error al enviar la solicitud:", err);
            alert("Error de conexión con el servidor.");
        }
    });
}

// =============================
// 📨 OBTENER SOLICITUDES
// =============================
async function obtener_solicitudes_estudiante(targetIdEstudiante) {
    try {
        const res = await fetchAPI(`/solicitudes-consultas`);
        if (!res || !res.ok) return;

        const data = await res.json();
        const listaGeneral = Array.isArray(data) ? data : (data.content || data.solicitudes || data.consultas || []);

        todasLasSolicitudes = listaGeneral.filter(s =>
            String(s.estudianteId) === String(targetIdEstudiante) ||
            String(s.identificacionEstudiante) === String(targetIdEstudiante)
        );

        actualizarTablaSolicitudes(todasLasSolicitudes);
    } catch (err) {
        console.error("Error al obtener solicitudes:", err);
        actualizarTablaSolicitudes([]);
    }
}

// =============================
// 📋 OBTENER CONSULTAS
// =============================
async function obtener_consultas_estudiante(targetId) {
    try {
        const res = await fetchAPI(`/solicitudes-consultas`);
        if (!res || !res.ok) return;

        const data = await res.json();
        const lista = Array.isArray(data) ? data : (data.content || data.consultas || []);

        consultas = lista.filter(c =>
            (String(c.estudianteId) === String(targetId) || String(c.identificacionEstudiante) === String(targetId))
        );

        actualizarTablaConsultas(consultas);
    } catch (error) {
        console.error("Error al obtener consultas:", error);
    }
}

// =============================
// 🧾 TABLA SOLICITUDES
// =============================
function actualizarTablaSolicitudes(solicitudes) {
    const tbody = document.querySelector("#tablaSolicitudes tbody");
    if (!tbody) return;

    tbody.innerHTML = "";

    if (!solicitudes || solicitudes.length === 0) {
        const fila = tbody.insertRow();
        const celda = fila.insertCell(0);
        celda.colSpan = 10;
        celda.textContent = "⚠️ No hay solicitudes registradas.";
        celda.style.textAlign = "center";
        return;
    }

    solicitudes.forEach(s => {
        const fila = tbody.insertRow();
        fila.insertCell(0).textContent = s.id || "—";
        fila.insertCell(1).textContent = s.nombrePrograma || s.programa || "—";
        fila.insertCell(2).textContent = s.nombreModulo || s.moduloNombre || `Módulo ${s.moduloId || "—"}`;
        fila.insertCell(3).textContent = s.asunto || s.tema || "—";
        fila.insertCell(4).textContent = s.fechaConsulta || s.fecha || "—";
        fila.insertCell(5).textContent = s.horaConsulta || s.hora || "—";
        fila.insertCell(6).textContent = s.nombreRecursoFisico || s.lugarConsulta || "Sin asignar";
        fila.insertCell(7).textContent = s.estado || "PENDIENTE";
        fila.insertCell(8).textContent = s.nombreCompletoDocente || s.docenteNombre || (s.docenteId ? `ID Docente: ${s.docenteId}` : "—");
        fila.insertCell(9).textContent = s.comentarioDocente || s.observacion || "—";
    });
}

// =============================
// 🧾 TABLA CONSULTAS
// =============================
function actualizarTablaConsultas(lista) {
    const tabla = document.querySelector("#tablaconsultas tbody");
    if (!tabla) return;

    tabla.innerHTML = "";

    if (!lista || lista.length === 0) {
        const fila = tabla.insertRow();
        const celda = fila.insertCell(0);
        celda.colSpan = 7;
        celda.textContent = "✅ No tienes consultas registradas.";
        celda.style.textAlign = "center";
        return;
    }

    lista.forEach(consulta => {
        const fila = tabla.insertRow();
        fila.insertCell(0).textContent = consulta.id || "—";
        fila.insertCell(1).textContent = consulta.nombreModulo || `Módulo ${consulta.moduloId || ""}`;
        fila.insertCell(2).textContent = consulta.asunto || consulta.tema || "—";
        fila.insertCell(3).textContent = consulta.nombreRecursoFisico || consulta.lugar_consulta || "Sin asignar";
        fila.insertCell(4).textContent = consulta.fechaConsulta || consulta.fecha || "—";
        fila.insertCell(5).textContent = consulta.horaConsulta || consulta.hora || "—";
        fila.insertCell(6).textContent = consulta.nombreCompletoDocente || (consulta.docenteId ? `Docente ${consulta.docenteId}` : "—");
    });
}

// =============================
// 🔍 FILTRO
// =============================
function obtenerConsultasFiltradas() {
    const fecha = document.getElementById("buscarFecha")?.value;
    const hora = document.getElementById("buscarHora")?.value;
    const idDocenteFiltro = document.getElementById("buscarDocente")?.value;

    let filtradas = [...consultas];

    if (fecha) filtradas = filtradas.filter(c => (c.fechaConsulta || c.fecha) === fecha);
    if (hora) filtradas = filtradas.filter(c => (c.horaConsulta || c.hora) === hora);
    if (idDocenteFiltro) filtradas = filtradas.filter(c => String(c.docenteId) === String(idDocenteFiltro));

    actualizarTablaConsultas(filtradas);
}

// =============================
// 🛠️ UI Y SESIÓN
// =============================
function openTab(evt, tabName) {
    document.querySelectorAll(".tabcontent").forEach(tab => tab.style.display = "none");
    document.querySelectorAll(".tablink").forEach(btn => btn.classList.remove("active"));

    const target = document.getElementById(tabName);
    if (target) target.style.display = "block";
    if (evt) evt.currentTarget.classList.add("active");

    // Ajusta el renderizado del calendario al abrir su pestaña
    if (tabName === 'horarioDocente' && calendarInstance) {
        setTimeout(() => {
            calendarInstance.updateSize();
        }, 10);
    }
}

function cerrarSesion() {
    localStorage.clear();
    window.location.href = "index.html";
}