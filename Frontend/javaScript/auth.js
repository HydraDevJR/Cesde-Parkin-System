
// Verificar si hay un usuario autenticado
function verificarAutenticacion() {
    const usuarioLogeado = sessionStorage.getItem("usuarioLogeado");
    
    if (!usuarioLogeado) {
        // No hay sesión activa, redirigir al login
        alert("Debe iniciar sesión para acceder a esta página");
        window.location.href = "/Frontend/html/login.html";
        return null;
    }
    
    try {
        return JSON.parse(usuarioLogeado);
    } catch (error) {
        console.error("Error al parsear usuario:", error);
        sessionStorage.removeItem("usuarioLogeado");
        window.location.href = "/Frontend/html/login.html";
        return null;
    }
}

// Verificar rol específico
function verificarRol(rolesPermitidos) {
    const usuario = verificarAutenticacion();
    
    if (!usuario) {
        return false;
    }
    
    if (!rolesPermitidos.includes(usuario.rol)) {
        alert("No tiene permisos para acceder a esta página");
        
        // Redirigir según el rol del usuario
        if (usuario.rol === "Administrador") {
            window.location.href = "/Frontend/html/Administrativo/administrativo.html";
        } else if (usuario.rol === "Estudiante") {
            window.location.href = "/Frontend/html/Usuarios/panel_usuario.html";
        } else {
            window.location.href = "/Frontend/html/login.html";
        }
        
        return false;
    }
    
    return true;
}

// Obtener usuario actual
function obtenerUsuarioActual() {
    const usuarioLogeado = sessionStorage.getItem("usuarioLogeado");
    if (!usuarioLogeado) return null;
    
    try {
        return JSON.parse(usuarioLogeado);
    } catch (error) {
        console.error("Error al obtener usuario actual:", error);
        return null;
    }
}

// Cerrar sesión con confirmación
function cerrarSesion() {
    const confirmar = confirm("¿Está seguro que desea cerrar sesión?");
    
    if (confirmar) {
        // Limpiar toda la sesión
        sessionStorage.clear();
        
        // Mostrar mensaje temporal
        console.log("Sesión cerrada exitosamente");
        
        // Redirigir al login
        window.location.href = "/Frontend/html/login.html";
    }
}

// Cerrar sesión sin confirmación (para uso directo)
function cerrarSesionDirecto() {
    sessionStorage.clear();
    window.location.href = "/Frontend/html/login.html";
}


// Prevenir que el usuario regrese con el botón atrás después de cerrar sesión
function prevenirRetroceso() {
    window.history.pushState(null, "", window.location.href);
    window.onpopstate = function() {
        window.history.pushState(null, "", window.location.href);
    };
}

// Verificar autenticación al cargar la página
document.addEventListener("DOMContentLoaded", function() {
    const usuario = verificarAutenticacion();
    
    if (usuario) {
        console.log("Usuario autenticado:", usuario.nombre);
        
        // Opcional: Mostrar información del usuario en la interfaz
        const nombreUsuarioElement = document.getElementById("nombreUsuario");
        if (nombreUsuarioElement) {
            nombreUsuarioElement.textContent = usuario.nombre;
        }
        
        // Prevenir retroceso después de cerrar sesión
        prevenirRetroceso();
    }
});