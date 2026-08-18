// =======================================
// 🌐 Configuración Inicial y Sesión
// =======================================
const API_URL = "http://localhost:8080";
const token = localStorage.getItem('token');
const rolUsuario = localStorage.getItem('rol');
const nombreUsuario = localStorage.getItem('nombre');

let todasLasConsultas = [];
let todosLosUsuarios = [];
let usuarioAEditar = null;

// Validación de sesión
if (!token || rolUsuario !== "ADMINISTRADOR") {
    cerrarSesion();
} else {
    const nombreDiv = document.getElementById("nombreUsuarioHeader");
    if (nombreDiv && nombreUsuario) nombreDiv.textContent = `Hola, ${nombreUsuario}`;
}

// Client HTTP Centralizado
async function fetchAuth(endpoint, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
        ...options.headers
    };

    try {
        const response = await fetch(`${API_URL}${endpoint}`, { ...options, headers });
        if (response.status === 401 || response.status === 403) {
            alert("Tu sesión ha expirado o no tienes permisos.");
            cerrarSesion();
            return null;
        }
        return response;
    } catch (error) {
        console.error(`Error HTTP (${endpoint}):`, error);
        throw error;
    }
}

// Carga de Eventos
document.addEventListener("DOMContentLoaded", () => {
    obtenerSolicitudConsultas();
    obtenerUsuarios();
    obtenerProgramas();
    obtenerModulos();
    obtenerSedes();        // Carga listado de sedes y llena el select de bloques
    obtenerBloques();      // Carga listado de bloques y llena el select de recursos
    obtenerRecursos();     // Cargar listado de recursos físicos
    cargarProgramasSelect();
    registrarUsuario();

    document.getElementById("registro_programas")?.addEventListener("submit", e => {
        e.preventDefault();
        registrarPrograma();
    });

    document.getElementById("registroModuloForm")?.addEventListener("submit", e => {
        e.preventDefault();
        registrarModulo();
    });

    document.getElementById("registroSedeForm")?.addEventListener("submit", e => {
        e.preventDefault();
        registrarSede();
    });

    document.getElementById("registroBloqueForm")?.addEventListener("submit", e => {
        e.preventDefault();
        registrarBloque();
    });

    document.getElementById("registroRecursoForm")?.addEventListener("submit", e => {
        e.preventDefault();
        registrarRecurso();
    });

    document.getElementById("formEditarUsuario")?.addEventListener("submit", guardarEdicionUsuario);
});

// =======================================
// 📥 Consultas
// =======================================
async function obtenerSolicitudConsultas() {
    try {
        const res = await fetchAuth('/solicitudes-consultas');
        if (!res || !res.ok) return;

        const data = await res.json();
        todasLasConsultas = Array.isArray(data) ? data : (data.content || data.consultas || data.solicitudes || []);
        actualizarTablaConsultas(todasLasConsultas);
    } catch (error) {
        console.error("Error al obtener consultas:", error);
    }
}

function actualizarTablaConsultas(consultas) {
    const tbody = document.querySelector("#tablaConsultas tbody, #tabla_consultas tbody, #tablaSolicitudes tbody");
    if (!tbody) return;
    tbody.innerHTML = "";

    if (!consultas || consultas.length === 0) {
        tbody.innerHTML = `<tr><td colspan="10" style="text-align:center;">No hay solicitudes registradas.</td></tr>`;
        return;
    }

    consultas.forEach(c => {
        const fila = tbody.insertRow();
        fila.insertCell(0).textContent = c.id || c.numeroConsulta || "N/A";
        fila.insertCell(1).textContent = c.nombreCompletoDocente || c.nombreDocente || "Sin nombre";
        fila.insertCell(2).textContent = c.nombreCompletoEstudiante || c.nombreEstudiante || "Sin nombre";
        fila.insertCell(3).textContent = c.nombreModulo || "N/A";
        fila.insertCell(4).textContent = c.asunto || c.descripcion || "N/A";
        fila.insertCell(5).textContent = c.nombrePrograma || "N/A";
        fila.insertCell(6).textContent = c.fechaConsulta || "N/A";
        fila.insertCell(7).textContent = c.horaConsulta || "N/A";
        fila.insertCell(8).textContent = c.nombreRecursoFisico || "Sin asignar";

        const celdaFirma = fila.insertCell(9);
        const firma = c.firma ? String(c.firma).trim() : "";

        if (firma && firma !== "No Firmado") {
            if (firma.toLowerCase().includes("qr")) {
                celdaFirma.innerHTML = `<span style="color:#007bff; font-weight:bold;">📱 Firmado por QR</span>`;
            } else if (firma.startsWith("data:image")) {
                celdaFirma.innerHTML = `<img src="${firma}" alt="Firma" style="max-width:100px; max-height:50px;"/>`;
            } else {
                celdaFirma.innerHTML = `<span style="color:orange;">⚠️ Formato no reconocido</span>`;
            }
        } else {
            celdaFirma.innerHTML = `<span style="color:red; font-weight:bold;">❌ No Firmado</span>`;
        }
    });
}

function obtenerConsultasFiltradas() {
    const fecha = document.getElementById("buscarFecha")?.value;
    const hora = document.getElementById("buscarHora")?.value;
    const mes = document.getElementById("buscarMes")?.value;
    const docente = document.getElementById("buscarIdDocente")?.value.trim().toLowerCase();
    const estudiante = document.getElementById("buscarIdEstudiante")?.value.trim().toLowerCase();

    let filtrados = todasLasConsultas.filter(c => {
        if (fecha && c.fechaConsulta !== fecha) return false;
        if (hora && !c.horaConsulta?.startsWith(hora)) return false;
        if (mes && parseInt(c.fechaConsulta?.split("-")[1]) !== parseInt(mes)) return false;
        if (docente && !String(c.identificacionDocente || c.nombreCompletoDocente || "").toLowerCase().includes(docente)) return false;
        if (estudiante && !String(c.identificacionEstudiante || c.nombreCompletoEstudiante || "").toLowerCase().includes(estudiante)) return false;
        return true;
    });

    actualizarTablaConsultas(filtrados);
}

// =======================================
// 👥 Usuarios (Listar, Editar, Inactivar)
// =======================================
async function obtenerUsuarios() {
    const res = await fetchAuth('/usuarios');
    if (!res || !res.ok) return;

    const data = await res.json();
    todosLosUsuarios = Array.isArray(data) ? data : (data.usuarios || []);
    actualizarTablaUsuarios(todosLosUsuarios);
}

function actualizarTablaUsuarios(usuarios) {
    const tbody = document.querySelector("#tablaUsuarios tbody");
    if (!tbody) return;
    tbody.innerHTML = "";

    if (!usuarios || usuarios.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">No hay usuarios registrados.</td></tr>`;
        return;
    }

    usuarios.forEach(u => {
        const fila = tbody.insertRow();
        const estaActivo = u.activo !== undefined ? Boolean(u.activo) : (u.estado === true || u.estado === "ACTIVO" || u.estado === 1);

        const programaTexto = u.nombrePrograma || u.programa?.nombre || u.programaNombre || (typeof u.programa === 'string' ? u.programa : null) || (u.rol === "ESTUDIANTE" ? "Sin programa" : "N/A");

        fila.insertCell(0).textContent = u.identificacion || u.id || "N/A";
        fila.insertCell(1).textContent = u.nombreCompleto || `${u.nombre || ''} ${u.apellido || ''}`.trim() || "Sin nombre";
        fila.insertCell(2).textContent = u.correo || u.email || "N/A";
        fila.insertCell(3).textContent = u.rol || "N/A";
        fila.insertCell(4).textContent = programaTexto;

        const celdaEstado = fila.insertCell(5);
        celdaEstado.textContent = estaActivo ? "Activo" : "Inactivo";
        celdaEstado.style.cssText = `font-weight:bold; color:${estaActivo ? 'green' : 'red'};`;

        const celdaAcciones = fila.insertCell(6);

        const btnEditar = document.createElement("button");
        btnEditar.textContent = "Editar";
        btnEditar.style.marginRight = "5px";
        btnEditar.onclick = () => abrirModalEditar(u);

        const btnEstado = document.createElement("button");
        btnEstado.textContent = estaActivo ? "Inactivar" : "Activar";
        btnEstado.style.cssText = `background-color:${estaActivo ? '#dc3545' : '#28a745'}; color:white; border:none; padding:4px 8px; border-radius:4px; cursor:pointer;`;
        btnEstado.onclick = () => cambiarEstadoUsuario(u, estaActivo);

        celdaAcciones.append(btnEditar, btnEstado);
    });
}

function obtenerUsuariosFiltrados() {
    const doc = document.getElementById("documento_usuario")?.value.trim();
    const rol = document.getElementById("filtroRolUsuario")?.value.trim();
    const estado = document.getElementById("filtroEstadoUsuario")?.value;

    let filtrados = todosLosUsuarios.filter(u => {
        if (doc && !String(u.id || u.identificacion || '').includes(doc)) return false;
        if (rol && String(u.rol || '').toUpperCase() !== rol.toUpperCase()) return false;
        if (estado !== "" && estado !== null) {
            const esActivo = u.activo !== undefined ? Boolean(u.activo) : (u.estado === true || u.estado === "ACTIVO");
            if (esActivo !== (estado === "true")) return false;
        }
        return true;
    });

    actualizarTablaUsuarios(filtrados);
}

function abrirModalEditar(usuario) {
    usuarioAEditar = usuario;
    const modal = document.getElementById("modalEditarUsuario");
    if (!modal) return;

    const partesNombre = (usuario.nombreCompleto || `${usuario.nombre || ''} ${usuario.apellido || ''}`).trim().split(" ");
    const nombreSolo = usuario.nombre || partesNombre[0] || "";

    document.getElementById("editUserId").value = usuario.id || usuario.identificacion;
    document.getElementById("editNombreUsuario").value = nombreSolo;
    document.getElementById("editCorreoUsuario").value = usuario.correo || usuario.email || "";

    const selectRol = document.getElementById("editRolUsuario");
    if (selectRol) {
        selectRol.value = (usuario.rol || "ESTUDIANTE").toUpperCase();
    }

    modal.showModal();
}

function cerrarModalEditar() {
    usuarioAEditar = null;
    document.getElementById("modalEditarUsuario")?.close();
}

async function guardarEdicionUsuario(e) {
    e.preventDefault();
    if (!usuarioAEditar) return;

    const idPK = usuarioAEditar.id || usuarioAEditar.identificacion;
    const nombre = document.getElementById("editNombreUsuario").value.trim();
    const correo = document.getElementById("editCorreoUsuario").value.trim();
    const nuevoRol = document.getElementById("editRolUsuario").value.toUpperCase();

    const partesNombre = (usuarioAEditar.nombreCompleto || "").trim().split(" ");
    const apellidoVal = usuarioAEditar.apellido || partesNombre.slice(1).join(" ") || " ";
    const programaIdVal = usuarioAEditar.programaId || usuarioAEditar.programa?.id || null;

    const payload = {
        id: usuarioAEditar.id || null,
        identificacion: String(usuarioAEditar.identificacion || idPK),
        nombre: nombre || usuarioAEditar.nombre || "Usuario",
        apellido: apellidoVal,
        correo: correo,
        email: correo,
        rol: nuevoRol,
        programaId: nuevoRol === "ESTUDIANTE" ? programaIdVal : null,
        activo: usuarioAEditar.activo !== undefined ? Boolean(usuarioAEditar.activo) : true
    };

    try {
        const res = await fetchAuth(`/usuarios/${idPK}`, {
            method: "PUT",
            body: JSON.stringify(payload)
        });

        if (res && res.ok) {
            alert("Usuario actualizado correctamente.");
            cerrarModalEditar();
            obtenerUsuarios();
        } else {
            const errData = await res?.json().catch(() => null);
            alert(`⚠️ Error al actualizar: ${errData?.message || errData?.error || 'Revisa la consola.'}`);
        }
    } catch (error) {
        console.error("Error al guardar edición:", error);
        alert("Ocurrió un error al procesar la actualización.");
    }
}

async function cambiarEstadoUsuario(usuario, estadoActual) {
    const nuevoEstado = !estadoActual;
    const accionText = nuevoEstado ? "activar" : "inactivar";
    const idPK = usuario.id || usuario.identificacion;

    if (!idPK) return alert("❌ Error: No se encontró el ID del usuario.");
    if (!confirm(`¿Estás seguro de que deseas ${accionText} a este usuario?`)) return;

    const partesNombre = (usuario.nombreCompleto || `${usuario.nombre || ''} ${usuario.apellido || ''}`).trim().split(" ");
    const nombreVal = usuario.nombre || partesNombre[0] || "Usuario";
    const apellidoVal = usuario.apellido || partesNombre.slice(1).join(" ") || " ";

    const payload = {
        id: usuario.id || null,
        identificacion: String(usuario.identificacion || idPK),
        nombre: nombreVal,
        apellido: apellidoVal,
        correo: usuario.correo || usuario.email || "",
        email: usuario.correo || usuario.email || "",
        rol: (usuario.rol || "ESTUDIANTE").toUpperCase(),
        programaId: usuario.programaId || usuario.programa?.id || null,
        activo: nuevoEstado,
        estado: nuevoEstado
    };

    try {
        let res = await fetchAuth(`/usuarios/${idPK}`, {
            method: "PUT",
            body: JSON.stringify(payload)
        });

        if (res && !res.ok && res.status === 405) {
            res = await fetchAuth(`/usuarios/${idPK}/estado`, {
                method: "PATCH",
                body: JSON.stringify({ activo: nuevoEstado })
            });
        }

        if (res && res.ok) {
            alert(`Usuario ${nuevoEstado ? "activado" : "inactivado"} correctamente.`);
            obtenerUsuarios();
        } else {
            const errData = await res?.json().catch(() => null);
            alert(`⚠️ Error al ${accionText}: ${errData?.message || errData?.error || 'Verifica los logs.'}`);
        }
    } catch (error) {
        console.error(`Error al ${accionText} usuario:`, error);
        alert("Ocurrió un error al cambiar el estado.");
    }
}

// =======================================
// 📝 Registro de Usuarios, Programas y Módulos
// =======================================
function registrarUsuario() {
    const form = document.getElementById("registroUsuarioForm");
    if (!form) return;

    const selectRol = document.getElementById("rolUsuario");
    const selectPrograma = document.getElementById("programaAcademico");

    if (selectRol && selectPrograma) {
        selectPrograma.disabled = true;
        selectRol.addEventListener("change", function () {
            const esEstudiante = this.value.toUpperCase() === "ESTUDIANTE";
            selectPrograma.disabled = !esEstudiante;
            if (!esEstudiante) selectPrograma.value = "";
        });
    }

    form.addEventListener("submit", async e => {
        e.preventDefault();

        const rol = form.rolUsuario.value.trim();
        const identificacion = form.idUsuario.value.trim();
        const programaVal = (rol.toUpperCase() === "ESTUDIANTE" && selectPrograma?.value) ? parseInt(selectPrograma.value) : null;

        const payload = {
            identificacion,
            nombre: form.nombreUsuario.value.trim(),
            apellido: form.apellidoUsuario ? form.apellidoUsuario.value.trim() : "",
            correo: form.correoUsuario ? form.correoUsuario.value.trim() : "",
            password: form.contraUsuario.value.trim(),
            rol: rol.toUpperCase(),
            programaId: programaVal,
            activo: true
        };

        const res = await fetchAuth('/auth/register', {
            method: "POST",
            body: JSON.stringify(payload)
        });

        if (res && res.ok) {
            alert("Usuario registrado correctamente");
            form.reset();
            if (selectPrograma) selectPrograma.disabled = true;
            obtenerUsuarios();
        } else {
            const errData = await res?.json().catch(() => null);
            alert(`⚠️ Error al registrar: ${errData?.message || errData?.error || 'Verifica la información.'}`);
        }
    });
}

async function obtenerProgramas() {
    const res = await fetchAuth('/programas');
    if (!res || !res.ok) return;

    const data = await res.json();
    const lista = Array.isArray(data) ? data : (data.programas || []);

    const tbody = document.querySelector("#tablaprogramas tbody");
    if (tbody) {
        tbody.innerHTML = "";
        lista.forEach(p => {
            const fila = tbody.insertRow();
            fila.insertCell(0).textContent = p.id;
            fila.insertCell(1).textContent = p.nombre || p.nombrePrograma;
        });
    }
}

async function registrarPrograma() {
    const nombre = document.getElementById("nombrePrograma")?.value.trim();
    if (!nombre) return alert("⚠️ El nombre del programa es obligatorio.");

    const res = await fetchAuth('/programas', {
        method: "POST",
        body: JSON.stringify({ nombre })
    });

    if (res && res.ok) {
        alert("Programa registrado correctamente");
        document.getElementById("registro_programas")?.reset();
        obtenerProgramas();
        cargarProgramasSelect();
    }
}

async function obtenerModulos() {
    const res = await fetchAuth('/modulos');
    if (!res || !res.ok) return;

    const data = await res.json();
    const lista = Array.isArray(data) ? data : (data.modulos || []);

    const tbody = document.querySelector("#tablamodulos tbody");
    if (tbody) {
        tbody.innerHTML = "";
        lista.forEach(m => {
            const fila = tbody.insertRow();
            fila.insertCell(0).textContent = m.id || m.idModulo || "N/A";
            fila.insertCell(1).textContent = m.nombre || m.nombreModulo || "N/A";
        });
    }
}

async function registrarModulo() {
    const nombre = document.getElementById("nombreModulo")?.value.trim();
    const descripcion = document.getElementById("descripcionModulo")?.value.trim() || nombre;

    if (!nombre) return alert("⚠️ El nombre del módulo es obligatorio.");

    const res = await fetchAuth('/modulos', {
        method: "POST",
        body: JSON.stringify({ nombre, descripcion })
    });

    if (res && res.ok) {
        alert("Módulo registrado correctamente");
        document.getElementById("registroModuloForm")?.reset();
        obtenerModulos();
    }
}

// =======================================
// 🏢 Infraestructura: Sedes, Bloques y Recursos Físicos
// =======================================

// 1. Sedes
async function obtenerSedes() {
    try {
        const res = await fetchAuth('/sedes');
        if (!res || !res.ok) return;

        const data = await res.json();
        const listaSedes = Array.isArray(data) ? data : (data.sedes || data.content || []);

        // Cargar las sedes en el select del formulario de registro de bloques
        const selectSedeBloque = document.getElementById("selectSedeBloque");
        if (selectSedeBloque) {
            selectSedeBloque.innerHTML = '<option value="">Seleccione una sede</option>';
            listaSedes.forEach(s => {
                const opt = document.createElement("option");
                opt.value = s.id || s.idSede;
                opt.textContent = s.nombre || s.nombreSede;
                selectSedeBloque.appendChild(opt);
            });
        }
    } catch (error) {
        console.error("Error al obtener sedes:", error);
    }
}

async function registrarSede() {
    const idSede = document.getElementById("idSede")?.value.trim();
    const nombre = document.getElementById("nombreSede")?.value.trim();
    const ubicacion = document.getElementById("ubicacionSede")?.value.trim();

    if (!nombre || !ubicacion) {
        return alert("⚠️ Por favor completa todos los campos obligatorios de la sede.");
    }

    const payload = {
        id: idSede ? parseInt(idSede) : undefined,
        nombre: nombre,
        ubicacion: ubicacion
    };

    try {
        const res = await fetchAuth('/sedes', {
            method: "POST",
            body: JSON.stringify(payload)
        });

        if (res && res.ok) {
            alert("✅ Sede registrada correctamente.");
            document.getElementById("registroSedeForm")?.reset();
            obtenerSedes(); // Actualiza los selectores que dependen de las sedes
        } else {
            const errData = await res?.json().catch(() => null);
            alert(`⚠️ Error al registrar sede: ${errData?.message || 'Verifica los datos.'}`);
        }
    } catch (error) {
        console.error("Error al registrar sede:", error);
        alert("Ocurrió un error al conectar con el servidor.");
    }
}

// 2. Bloques
async function obtenerBloques() {
    try {
        const res = await fetchAuth('/bloques');
        if (!res || !res.ok) return;

        const data = await res.json();
        const listaBloques = Array.isArray(data) ? data : (data.bloques || data.content || []);

        // Cargar los bloques en el select del formulario de registro de recursos físicos
        const selectBloqueRecurso = document.getElementById("selectBloqueRecurso");
        if (selectBloqueRecurso) {
            selectBloqueRecurso.innerHTML = '<option value="">Seleccione un bloque</option>';
            listaBloques.forEach(b => {
                const opt = document.createElement("option");
                opt.value = b.id || b.idBloque;
                opt.textContent = b.nombre || b.nombreBloque;
                selectBloqueRecurso.appendChild(opt);
            });
        }
    } catch (error) {
        console.error("Error al obtener bloques:", error);
    }
}

async function registrarBloque() {
    const nombre = document.getElementById("nombreBloque")?.value.trim();
    const sedeId = document.getElementById("selectSedeBloque")?.value;

    if (!nombre || !sedeId) {
        return alert("⚠️ Por favor completa el nombre del bloque y selecciona una sede.");
    }

    const payload = {
        nombre: nombre,
        sedeId: parseInt(sedeId)
    };

    try {
        const res = await fetchAuth('/bloques', {
            method: "POST",
            body: JSON.stringify(payload)
        });

        if (res && res.ok) {
            alert("✅ Bloque registrado correctamente.");
            document.getElementById("registroBloqueForm")?.reset();
            obtenerBloques(); // Actualiza el select en el formulario de recursos
        } else {
            const errData = await res?.json().catch(() => null);
            alert(`⚠️ Error al registrar bloque: ${errData?.message || errData?.error || 'Verifica los datos.'}`);
        }
    } catch (error) {
        console.error("Error al registrar bloque:", error);
        alert("Ocurrió un error al conectar con el servidor.");
    }
}

// 3. Recursos Físicos
async function obtenerRecursos() {
    try {
        const res = await fetchAuth('/recursos-fisicos');
        if (!res || !res.ok) return;
        const data = await res.json();
        console.log("Recursos físicos cargados:", data);
    } catch (error) {
        console.error("Error al obtener recursos físicos:", error);
    }
}

async function registrarRecurso() {
    const nombre = document.getElementById("nombreRecurso")?.value.trim();
    const tipo = document.getElementById("tipoRecurso")?.value;
    const bloqueId = document.getElementById("selectBloqueRecurso")?.value;

    if (!nombre || !tipo || !bloqueId) {
        return alert("⚠️ Por favor completa los campos obligatorios (Nombre, Tipo y Bloque).");
    }

    // ⚠️ Corregido: 'bloqueId' en minúscula inicial para coincidir exactamente con el DTO de Kotlin
    const payload = {
        nombre: nombre,
        tipo: tipo,
        bloqueId: parseInt(bloqueId)
    };

    try {
        const res = await fetchAuth('/recursos-fisicos', {
            method: "POST",
            body: JSON.stringify(payload)
        });

        if (res && res.ok) {
            alert("✅ Recurso físico registrado correctamente.");
            document.getElementById("registroRecursoForm")?.reset();
            obtenerRecursos();
        } else {
            const errData = await res?.json().catch(() => null);
            alert(`⚠️ Error al registrar recurso: ${errData?.message || errData?.error || 'Verifica los datos.'}`);
        }
    } catch (error) {
        console.error("Error al registrar recurso físico:", error);
        alert("Error de conexión con el servidor.");
    }
}

// =======================================
// 🛠️ Helpers Generales
// =======================================
async function cargarProgramasSelect() {
    const select = document.getElementById('programaAcademico');
    if (!select) return;

    const res = await fetchAuth('/programas');
    if (!res || !res.ok) return;

    const data = await res.json();
    const listaProgramas = Array.isArray(data) ? data : (data.programas || []);

    select.innerHTML = '<option value="">Seleccione un programa</option>';
    listaProgramas.forEach(p => {
        const opt = document.createElement('option');
        opt.value = p.id || p.idPrograma;
        opt.textContent = p.nombre || p.nombrePrograma;
        select.appendChild(opt);
    });
}


function openTab(evt, tabName) {
    document.querySelectorAll(".tabcontent").forEach(tab => tab.classList.remove("active"));
    document.querySelectorAll(".tablink").forEach(btn => btn.classList.remove("active"));
    document.getElementById(tabName).classList.add("active");
    evt.currentTarget.classList.add("active");
}

function exportarExcel() {
    const tabla = document.getElementById("tablaConsultas");
    if (!tabla) return;
    const wb = XLSX.utils.table_to_book(tabla, { sheet: "Solicitud_Consultas" });
    XLSX.writeFile(wb, "Solicitud_Consultas.xlsx");
}

function cerrarSesion() {
    localStorage.clear();
    window.location.href = "index.html";
}

