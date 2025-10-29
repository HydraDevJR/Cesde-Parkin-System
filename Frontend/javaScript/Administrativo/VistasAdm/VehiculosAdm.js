const API_URL = "http://localhost:8080/api/vehiculos";
const Crear = document.getElementById("btn-crearVehiculo");
const mensaje = document.getElementById("mensaje");

const cedula = document.getElementById("cedula");
const placa = document.getElementById("placa");
const marca = document.getElementById("marca");
const modelo = document.getElementById("modelo");
const color = document.getElementById("color");
const tipo = document.getElementById("tipo");

const tbodyVehiculos = document.getElementById("tbodyVehiculos");

document.addEventListener("DOMContentLoaded", mostrarTablaVehiculos);
Crear.addEventListener("click", crearNuevoVehiculo);

// ---------------------- CREAR ----------------------
async function crearNuevoVehiculo() {
    if (!cedula.value || !placa.value || !marca.value || !modelo.value || !color.value || !tipo.value) {
        mensaje.innerHTML = `<span style="color:#e71d73;">Todos los campos son obligatorios</span>`;
        setTimeout(() => mensaje.innerHTML = "", 3000);
        return;
    }

    const nuevoVehiculo = {
        documento: parseInt(cedula.value),
        placa: placa.value.trim(),
        marca: marca.value.trim(),
        modelo: modelo.value.trim(),
        color: color.value.trim(),
        tipo: tipo.value,
    };

    try {
        const res = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nuevoVehiculo)
        });

        const text = await res.text();
        mensaje.innerHTML = `<span style="color:${res.ok ? '#28a745' : '#e71d73'};">${text}</span>`;
        setTimeout(() => mensaje.innerHTML = "", 3000);

        if (res.ok) {
            limpiarFormulario();
            mostrarTablaVehiculos();
        }
    } catch (err) {
        mensaje.innerHTML = `<span style="color:#e71d73;">Error: ${err.message}</span>`;
        setTimeout(() => mensaje.innerHTML = "", 3000);
    }
}

// ---------------------- LISTAR ----------------------
async function mostrarTablaVehiculos() {
    try {
        const res = await fetch(API_URL);
        const vehiculos = await res.json();

        tbodyVehiculos.innerHTML = "";
        vehiculos.forEach(v => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${v.documento}</td>
                <td>${v.placa}</td>
                <td>${v.marca}</td>
                <td>${v.modelo}</td>
                <td>${v.color}</td>
                <td>${v.tipo}</td>
                <td>
                    <i class="bi bi-arrow-repeat icon" style="cursor:pointer;" onclick="editarVehiculo('${v.placa}')"></i>
                    <i class="bi bi-trash icon" style="cursor:pointer;" onclick="eliminarVehiculo('${v.placa}')"></i>
                </td>
            `;
            tbodyVehiculos.appendChild(fila);
        });
    } catch (err) {
        mensaje.innerHTML = `<span style="color:#e71d73;">Error al cargar vehículos</span>`;
        setTimeout(() => mensaje.innerHTML = "", 3000);
    }
}

// ---------------------- ELIMINAR ----------------------
async function eliminarVehiculo(placaVeh) {
    mensaje.innerHTML = `
        <span>¿Eliminar vehículo <b>${placaVeh}</b>?</span>
        <button id="btnSi" class="btn-crearUsuario">Sí</button>
        <button id="btnNo" class="btn-crearUsuario">No</button>
    `;

    document.getElementById("btnSi").addEventListener("click", async () => {
        try {
            const res = await fetch(`${API_URL}/${placaVeh}`, { method: "DELETE" });
            const text = await res.text();
            mensaje.innerHTML = `<span style="color:${res.ok ? '#28a745' : '#e71d73'};">${text}</span>`;
            if (res.ok) mostrarTablaVehiculos();
        } catch (err) {
            mensaje.innerHTML = `<span style="color:#e71d73;">Error: ${err.message}</span>`;
        } finally {
            setTimeout(() => mensaje.innerHTML = "", 3000);
        }
    });

    document.getElementById("btnNo").addEventListener("click", () => {
        mensaje.innerHTML = `<span style="color:#e71d73;">Cancelado</span>`;
        setTimeout(() => mensaje.innerHTML = "", 3000);
    });
}

// ---------------------- EDITAR ----------------------
async function editarVehiculo(placaVeh) {
    try {
        const res = await fetch(`${API_URL}/${placaVeh}`);
        if (!res.ok) throw new Error("Vehículo no encontrado");

        const v = await res.json();

        cedula.value = v.documento;
        placa.value = v.placa;
        marca.value = v.marca;
        modelo.value = v.modelo;
        color.value = v.color;
        tipo.value = v.tipo;

        Crear.textContent = "Actualizar";
        Crear.removeEventListener("click", crearNuevoVehiculo);
        Crear.addEventListener("click", async function guardarCambios() {
            const actualizado = {
                documento: parseInt(cedula.value),
                placa: placa.value.trim(),
                marca: marca.value.trim(),
                modelo: modelo.value.trim(),
                color: color.value.trim(),
                tipo: tipo.value,
                fechaRegistro: v.fechaRegistro
            };

            try {
                const res = await fetch(`${API_URL}/${placaVeh}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(actualizado)
                });
                const text = await res.text();
                mensaje.innerHTML = `<span style="color:${res.ok ? '#28a745' : '#e71d73'};">${text}</span>`;
                if (res.ok) mostrarTablaVehiculos(); limpiarFormulario();
            } catch (err) {
                mensaje.innerHTML = `<span style="color:#e71d73;">Error: ${err.message}</span>`;
            } finally {
                setTimeout(() => mensaje.innerHTML = "", 3000);
                Crear.textContent = "Registrar";
                Crear.removeEventListener("click", guardarCambios);
                Crear.addEventListener("click", crearNuevoVehiculo);
                limpiarFormulario();
            }
        });
    } catch (err) {
        mensaje.innerHTML = `<span style="color:#e71d73;">${err.message}</span>`;
        setTimeout(() => mensaje.innerHTML = "", 3000);
    }
}

// ---------------------- UTILIDAD ----------------------
function limpiarFormulario() {
    cedula.value = "";
    placa.value = "";
    marca.value = "";
    modelo.value = "";
    color.value = "";
    tipo.value = "Carro";
}

verificarRol(["Administrador"]);

document.addEventListener("DOMContentLoaded", function() {
    // Cerrar sesión directamente
    const btnCerrarSesion = document.getElementById("cerrarSesion");
    if (btnCerrarSesion) {
        btnCerrarSesion.addEventListener("click", function(e) {
            e.preventDefault();
            cerrarSesionDirecto(); // Función del auth.js
        });
        
        btnCerrarSesion.style.cursor = "pointer";
    }
});