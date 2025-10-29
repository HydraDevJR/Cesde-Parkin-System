const API_USUARIOS = "http://localhost:8080/api/usuarios";
const API_VEHICULOS = "http://localhost:8080/api/vehiculos";

// Elementos del DOM
const nombreU = document.getElementById("nombreU");
const correoU = document.getElementById("correoU");
const carreraName = document.getElementById("carreraName");
const ultimaPlaca = document.getElementById("ultimaPlaca");

const editarDatos = document.getElementById("editarDatos");
const btnEditar = document.getElementById("editar");
const btnGuardar = document.getElementById("guardar");
const btnCancelar = document.getElementById("cancelar");

const inputCorreo = document.getElementById("correo");
const inputContrasenaActual = document.getElementById("contrasenaActual");
const inputContrasenaNueva = document.getElementById("contrasenaNueva");
const btnCerrarSesion = document.getElementById("cerrarSesion");

let usuarioActual = null;

// ==================== INICIALIZACIÓN ====================
document.addEventListener("DOMContentLoaded", async function() {
    // Verificar autenticación
    verificarRol(["Estudiante"]);
    
    usuarioActual = obtenerUsuarioActual();
    if (!usuarioActual) {
        window.location.href = "/Frontend/html/login.html";
        return;
    }

    // Cargar datos del usuario
    await cargarDatosUsuario();
    
    // Configurar eventos
    configurarEventos();
});

// ==================== CARGAR DATOS ====================
async function cargarDatosUsuario() {
    try {
        // Obtener datos actualizados del usuario desde la BD
        const response = await fetch(`${API_USUARIOS}/${usuarioActual.documento}`);
        
        if (!response.ok) {
            throw new Error("Error al cargar datos del usuario");
        }
        
        const usuario = await response.json();
        
        // Actualizar sessionStorage
        sessionStorage.setItem("usuarioLogeado", JSON.stringify(usuario));
        usuarioActual = usuario;
        
        // Mostrar datos en la interfaz
        nombreU.textContent = usuario.nombre;
        correoU.textContent = usuario.correo;
        
        // Carrera (campo que no existe en BD)
        carreraName.textContent = "Desarrollo de Software"; // Temporal
        
        // Cargar último vehículo
        await cargarUltimoVehiculo(usuario.documento);
        
    } catch (error) {
        console.error("Error al cargar datos:", error);
        mostrarMensaje("Error al cargar los datos del usuario", "error");
    }
}

async function cargarUltimoVehiculo(documento) {
    try {
        const response = await fetch(API_VEHICULOS);
        
        if (!response.ok) {
            throw new Error("Error al cargar vehículos");
        }
        
        const vehiculos = await response.json();
        
        // Filtrar vehículos del usuario actual
        const vehiculosUsuario = vehiculos.filter(v => v.documento === documento);
        
        if (vehiculosUsuario.length > 0) {
            // Obtener el último vehículo registrado
            const ultimoVehiculo = vehiculosUsuario[vehiculosUsuario.length - 1];
            ultimaPlaca.textContent = ultimoVehiculo.placa;
        } else {
            ultimaPlaca.textContent = "Sin vehículos";
        }
        
    } catch (error) {
        console.error("Error al cargar vehículos:", error);
        ultimaPlaca.textContent = "Error al cargar";
    }
}

// ==================== EVENTOS ====================
function configurarEventos() {
    // Botón Editar
    btnEditar.addEventListener("click", mostrarFormularioEdicion);
    
    // Botón Cancelar
    btnCancelar.addEventListener("click", cancelarEdicion);
    
    // Botón Guardar
    btnGuardar.addEventListener("click", guardarCambios);
    
    // Cerrar sesión
    if (btnCerrarSesion) {
        btnCerrarSesion.addEventListener("click", function(e) {
            e.preventDefault();
            cerrarSesionDirecto();
        });
        btnCerrarSesion.style.cursor = "pointer";
    }
    
}

// ==================== FUNCIONES DE EDICIÓN ====================
function mostrarFormularioEdicion() {
    // Llenar los campos con los datos actuales
    inputCorreo.value = usuarioActual.correo;
    inputContrasenaActual.value = "";
    inputContrasenaNueva.value = "";
    
    // Invertir displays
    editarDatos.style.display = "flex";
    btnEditar.style.display = "none";
}

function cancelarEdicion() {
    // Limpiar campos
    inputCorreo.value = "";
    inputContrasenaActual.value = "";
    inputContrasenaNueva.value = "";
    
    // Limpiar mensaje
    const mensajeElement = document.getElementById("mensaje");
    if (mensajeElement) {
        mensajeElement.textContent = "";
    }
    
    // Invertir displays
    editarDatos.style.display = "none";
    btnEditar.style.display = "block";
}

async function guardarCambios() {
    // Validaciones
    const nuevoCorreo = inputCorreo.value.trim();
    const contrasenaActual = inputContrasenaActual.value.trim();
    const contrasenaNueva = inputContrasenaNueva.value.trim();
    
    if (!nuevoCorreo) {
        mostrarMensaje("El correo no puede estar vacío", "error");
        return;
    }
    
    // Validar formato de correo
    const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regexCorreo.test(nuevoCorreo)) {
        mostrarMensaje("El formato del correo no es válido", "error");
        return;
    }
    
    // Si quiere cambiar contraseña, debe proporcionar ambas
    if (contrasenaNueva && !contrasenaActual) {
        mostrarMensaje("Debe ingresar la contraseña actual para cambiarla", "error");
        return;
    }
    
    if (contrasenaActual && !contrasenaNueva) {
        mostrarMensaje("Debe ingresar la nueva contraseña", "error");
        return;
    }
    
    // Validar contraseña actual si quiere cambiarla
    if (contrasenaActual && usuarioActual.contrasena !== contrasenaActual) {
        mostrarMensaje("La contraseña actual es incorrecta", "error");
        return;
    }
    
    try {
        // Preparar datos actualizados
        const datosActualizados = {
            documento: usuarioActual.documento,
            nombre: usuarioActual.nombre,
            correo: nuevoCorreo,
            contrasena: contrasenaNueva || usuarioActual.contrasena,
            rol: usuarioActual.rol,
            estado: usuarioActual.estado
        };
        
        // Enviar actualización al backend
        const response = await fetch(`${API_USUARIOS}/${usuarioActual.documento}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(datosActualizados)
        });
        
        const mensaje = await response.text();
        
        if (response.ok) {
            mostrarMensaje("Datos actualizados correctamente", "success");
            
            // Actualizar sessionStorage
            usuarioActual.correo = nuevoCorreo;
            if (contrasenaNueva) {
                usuarioActual.contrasena = contrasenaNueva;
            }
            sessionStorage.setItem("usuarioLogeado", JSON.stringify(usuarioActual));
            
            // Actualizar interfaz
            correoU.textContent = nuevoCorreo;
            
            // Cerrar formulario después de un delay
            setTimeout(() => {
                cancelarEdicion();
            }, 1500);
            
        } else {
            mostrarMensaje(mensaje, "error");
        }
        
    } catch (error) {
        console.error("Error al actualizar:", error);
        mostrarMensaje("Error de conexión con el servidor", "error");
    }
}

// ==================== UTILIDADES ====================
function mostrarMensaje(texto, tipo) {
    const mensajeElement = document.getElementById("mensaje");
    
    if (!mensajeElement) {
        console.error("Elemento #mensaje no encontrado");
        return;
    }
    
    // Estilos según el tipo
    if (tipo === "success") {
        mensajeElement.style.color = "#28a745";
    } else {
        mensajeElement.style.color = "#e71d73";
    }
    
    mensajeElement.textContent = texto;
    
    // Limpiar mensaje después de 2 segundos
    setTimeout(() => {
        mensajeElement.textContent = "";
    }, 2000);
}