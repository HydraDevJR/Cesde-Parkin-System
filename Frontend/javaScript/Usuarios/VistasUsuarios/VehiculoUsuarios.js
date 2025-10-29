const API_VEHICULOS = "http://localhost:8080/api/vehiculos";

// Elementos del DOM
const editarDatos = document.getElementById("editarDatos");
const btnAgregarVehiculo = document.getElementById("agregarVehiculo");
const btnRegistrar = document.getElementById("registrar");
const btnCancelar = document.getElementById("cancelar");

const inputPlaca = document.getElementById("placa");
const inputMarca = document.getElementById("marca");
const inputModelo = document.getElementById("modelo");
const inputColor = document.getElementById("color");
const inputTipo = document.getElementById("tipo");

const tbodyVehiculos = document.getElementById("tbodyVehiculos");
const btnCerrarSesion = document.getElementById("cerrarSesion");

let usuarioActual = null;
let modoEdicion = false;
let placaOriginal = null;

// ==================== INICIALIZACIÓN ====================
document.addEventListener("DOMContentLoaded", async function() {
    // Verificar autenticación
    verificarRol(["Estudiante"]);
    
    usuarioActual = obtenerUsuarioActual();
    if (!usuarioActual) {
        window.location.href = "/Frontend/html/login.html";
        return;
    }

    // Cargar vehículos del usuario
    await cargarVehiculos();
    
    // Configurar eventos
    configurarEventos();
});

// ==================== CARGAR VEHÍCULOS ====================
async function cargarVehiculos() {
    try {
        const response = await fetch(API_VEHICULOS);
        
        if (!response.ok) {
            throw new Error("Error al cargar vehículos");
        }
        
        const vehiculos = await response.json();
        
        // Filtrar solo los vehículos del usuario actual
        const vehiculosUsuario = vehiculos.filter(v => v.documento === usuarioActual.documento);
        
        // Mostrar en la tabla
        mostrarVehiculos(vehiculosUsuario);
        
    } catch (error) {
        console.error("Error al cargar vehículos:", error);
        mostrarMensaje("Error al cargar los vehículos", "error");
    }
}

function mostrarVehiculos(vehiculos) {
    tbodyVehiculos.innerHTML = "";
    
    if (vehiculos.length === 0) {
        tbodyVehiculos.innerHTML = `
            <tr>
                <td colspan="6" style="padding: 20px; color: #666;">
                    No tienes vehículos registrados
                </td>
            </tr>
        `;
        return;
    }
    
    vehiculos.forEach(v => {
        const fila = document.createElement("tr");
        fila.innerHTML = `
            <td>${v.placa}</td>
            <td>${v.marca}</td>
            <td>${v.modelo}</td>
            <td>${v.color}</td>
            <td>${v.tipo}</td>
            <td>
                <i class="bi bi-arrow-repeat icon" style="cursor:pointer; font-size: 18px; margin-right: 10px;" onclick="editarVehiculo('${v.placa}')"></i>
                <i class="bi bi-trash icon" style="cursor:pointer; font-size: 18px;" onclick="eliminarVehiculo('${v.placa}')"></i>
            </td>
        `;
        tbodyVehiculos.appendChild(fila);
    });
}

// ==================== EVENTOS ====================
function configurarEventos() {
    // Botón Agregar Vehículo
    btnAgregarVehiculo.addEventListener("click", mostrarFormularioRegistro);
    
    // Botón Cancelar
    btnCancelar.addEventListener("click", cancelarFormulario);
    
    // Botón Registrar/Actualizar
    btnRegistrar.addEventListener("click", function() {
        if (modoEdicion) {
            actualizarVehiculo();
        } else {
            registrarVehiculo();
        }
    });
    
    // Cerrar sesión
    if (btnCerrarSesion) {
        btnCerrarSesion.addEventListener("click", function(e) {
            e.preventDefault();
            cerrarSesionDirecto();
        });
        btnCerrarSesion.style.cursor = "pointer";
    }
}

// ==================== MOSTRAR/OCULTAR FORMULARIO ====================
function mostrarFormularioRegistro() {
    modoEdicion = false;
    placaOriginal = null;
    
    // Limpiar campos
    limpiarFormulario();
    
    // Cambiar texto del botón
    btnRegistrar.textContent = "Registrar";
    
    // Invertir displays
    editarDatos.style.display = "flex";
    btnAgregarVehiculo.style.display = "none";
}

function cancelarFormulario() {
    modoEdicion = false;
    placaOriginal = null;
    
    // Limpiar campos y mensaje
    limpiarFormulario();
    limpiarMensaje();
    
    // Invertir displays
    editarDatos.style.display = "none";
    btnAgregarVehiculo.style.display = "block";
}

function limpiarFormulario() {
    inputPlaca.value = "";
    inputMarca.value = "";
    inputModelo.value = "";
    inputColor.value = "";
    inputTipo.value = "Carro";
}

function limpiarMensaje() {
    const mensajeElement = document.getElementById("mensaje");
    if (mensajeElement) {
        mensajeElement.textContent = "";
    }
}

// ==================== REGISTRAR VEHÍCULO ====================
async function registrarVehiculo() {
    // Validaciones
    const placa = inputPlaca.value.trim().toUpperCase();
    const marca = inputMarca.value.trim();
    const modelo = inputModelo.value.trim();
    const color = inputColor.value.trim();
    const tipo = inputTipo.value;
    
    if (!placa || !marca || !modelo || !color) {
        mostrarMensaje("Todos los campos son obligatorios", "error");
        return;
    }
    
    // Validar formato de placa (6 caracteres alfanuméricos)
    if (placa.length !== 6) {
        mostrarMensaje("La placa debe tener 6 caracteres", "error");
        return;
    }
    
    try {
        const nuevoVehiculo = {
            placa: placa,
            tipo: tipo,
            marca: marca,
            modelo: modelo,
            color: color,
            documento: usuarioActual.documento
        };
        
        const response = await fetch(API_VEHICULOS, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(nuevoVehiculo)
        });
        
        const mensaje = await response.text();
        
        if (response.ok) {
            mostrarMensaje("Vehículo registrado correctamente", "success");
            
            // Recargar lista de vehículos
            await cargarVehiculos();
            
            // Cerrar formulario después de un delay
            setTimeout(() => {
                cancelarFormulario();
            }, 1500);
            
        } else {
            mostrarMensaje(mensaje, "error");
        }
        
    } catch (error) {
        console.error("Error al registrar vehículo:", error);
        mostrarMensaje("Error de conexión con el servidor", "error");
    }
}

// ==================== EDITAR VEHÍCULO ====================
async function editarVehiculo(placa) {
    try {
        const response = await fetch(`${API_VEHICULOS}/${placa}`);
        
        if (!response.ok) {
            throw new Error("Error al cargar vehículo");
        }
        
        const vehiculo = await response.json();
        
        // Verificar que el vehículo pertenezca al usuario actual
        if (vehiculo.documento !== usuarioActual.documento) {
            mostrarMensaje("No tienes permisos para editar este vehículo", "error");
            return;
        }
        
        // Activar modo edición
        modoEdicion = true;
        placaOriginal = placa;
        
        // Llenar formulario con datos del vehículo
        inputPlaca.value = vehiculo.placa;
        inputMarca.value = vehiculo.marca;
        inputModelo.value = vehiculo.modelo;
        inputColor.value = vehiculo.color;
        inputTipo.value = vehiculo.tipo;
        
        // Cambiar texto del botón
        btnRegistrar.textContent = "Actualizar";
        
        // Mostrar formulario
        editarDatos.style.display = "flex";
        btnAgregarVehiculo.style.display = "none";
        
    } catch (error) {
        console.error("Error al cargar vehículo:", error);
        mostrarMensaje("Error al cargar el vehículo", "error");
    }
}

async function actualizarVehiculo() {
    // Validaciones
    const placa = inputPlaca.value.trim().toUpperCase();
    const marca = inputMarca.value.trim();
    const modelo = inputModelo.value.trim();
    const color = inputColor.value.trim();
    const tipo = inputTipo.value;
    
    if (!placa || !marca || !modelo || !color) {
        mostrarMensaje("Todos los campos son obligatorios", "error");
        return;
    }
    
    if (placa.length !== 6) {
        mostrarMensaje("La placa debe tener 6 caracteres", "error");
        return;
    }
    
    try {
        const vehiculoActualizado = {
            placa: placa,
            tipo: tipo,
            marca: marca,
            modelo: modelo,
            color: color,
            documento: usuarioActual.documento
        };
        
        const response = await fetch(`${API_VEHICULOS}/${placaOriginal}`, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(vehiculoActualizado)
        });
        
        const mensaje = await response.text();
        
        if (response.ok) {
            mostrarMensaje("Vehículo actualizado correctamente", "success");
            
            // Recargar lista de vehículos
            await cargarVehiculos();
            
            // Cerrar formulario después de un delay
            setTimeout(() => {
                cancelarFormulario();
            }, 1500);
            
        } else {
            mostrarMensaje(mensaje, "error");
        }
        
    } catch (error) {
        console.error("Error al actualizar vehículo:", error);
        mostrarMensaje("Error de conexión con el servidor", "error");
    }
}

// ==================== ELIMINAR VEHÍCULO ====================
async function eliminarVehiculo(placaVeh) {
    // Mostrar mensaje de confirmación con botones Sí/No
    mensaje.innerHTML = `
        <span style="color:black;">¿Eliminar vehículo <b>${placaVeh}</b>?</span>
        <button id="btnSi" class="btn">Sí</button>
        <button id="btnNo" class="btn">No</button>
    `;

    // Evento para confirmar eliminación
    document.getElementById("btnSi").addEventListener("click", async () => {
        try {
            const res = await fetch(`${API_VEHICULOS}/${placaVeh}`, { method: "DELETE" });
            const text = await res.text();

            mensaje.innerHTML = `<span style="color:${res.ok ? '#28a745' : '#e71d73'};">${text}</span>`;

            if (res.ok) {
                await cargarVehiculos(); // Recargar lista de vehículos
            }
        } catch (err) {
            mensaje.innerHTML = `<span style="color:#e71d73;">Error: ${err.message}</span>`;
        } finally {
            setTimeout(() => mensaje.innerHTML = "", 2000);
        }
    });

    // Evento para cancelar eliminación
    document.getElementById("btnNo").addEventListener("click", () => {
        mensaje.innerHTML = `<span style="color:#e71d73;">Cancelado</span>`;
        setTimeout(() => mensaje.innerHTML = "", 3000);
    });
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
    
    // Limpiar mensaje después de 3 segundos
    setTimeout(() => {
        mensajeElement.textContent = "";
    }, 3000);
}