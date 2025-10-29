// let Crear = document.getElementById("btn-crearUsuario");
// let mostrarUsuarios = document.getElementById("mostrarUsuarios");

// const mensaje = document.getElementById("mensaje");

// let cedula = document.getElementById("cedula");
// let nombre = document.getElementById("nombre");
// let correo = document.getElementById("correo");
// let contrasena = document.getElementById("contrasena");
// let rol = document.getElementById("rol");

// document.addEventListener("DOMContentLoaded", mostrarTablaUsuarios);

// Crear.addEventListener("click", crearNuevoUsuario);

// function crearNuevoUsuario() {
//     if (cedula.value != "" && nombre.value != "" && correo.value != "" && contrasena.value != "" && rol.value != "") {
//         const Usuarios = JSON.parse(localStorage.getItem("Usuarios")) || [];
        
//         //Validar si la cédula ya existe
//         const existeCedula = Usuarios.some(usuario => usuario.cedula === cedula.value);
//         if (existeCedula) {
//             mensaje.innerHTML = `<span style="color: #e71d73;">Ya existe un usuario con esta cédula</span>`;
//             setTimeout(() => mensaje.innerHTML = "", 2000);
//             return;
//         }

//         // Validar si el correo ya existe
//         const existeCorreo = Usuarios.some(usuario => usuario.correo === correo.value);
//         if (existeCorreo) {
//             mensaje.innerHTML = `<span style="color: #e71d73;">Ya existe un usuario con este correo</span>`;
//             setTimeout(() => mensaje.innerHTML = "", 2000);
//             return;
//         }

//         // Si no hay duplicados, crear el usuario
//         Usuarios.push({cedula: cedula.value, nombre: nombre.value, correo: correo.value, contrasena: contrasena.value, estado: "Activo", rol: rol.value});
//         localStorage.setItem("Usuarios", JSON.stringify(Usuarios));

//         mensaje.innerHTML = `<span style="color: #28a745;">Usuario creado exitosamente</span>`;
//         setTimeout(() => mensaje.innerHTML = "", 2000);

//         mostrarTablaUsuarios();

//         //agregarUsuario(cedula.value, nombre.value, correo.value, rol.value);

//         // Limpiar campos después de crear el usuario
//         cedula.value = "";
//         nombre.value = "";
//         correo.value = "";
//         contrasena.value = "";
//         rol.value = "";
        
//     }else{
//         mensaje.innerHTML = `<span style="color: #e71d73;">Todos los campos son obligatorios</span>`;
//         setTimeout(() => mensaje.innerHTML = "", 2000);
//     }
// };

// // function agregarUsuario(cedula, nombre, correo, rol) {

// //     let divUsuario = document.createElement("div");

// //     divUsuario.id = "usuarioItem";

// //     let pCedula = document.createElement("p");
// //     pCedula.textContent = cedula;

// //     let pNombre = document.createElement("p");
// //     pNombre.textContent = nombre;

// //     let pCorreo = document.createElement("p");
// //     pCorreo.textContent = correo;

// //     let pRol = document.createElement("p");
// //     pRol.textContent = rol;

// //     let pEstado = document.createElement("p");
// //     pEstado.textContent = "Activo";

// //     const iconActualizar = document.createElement("i");
// //     iconActualizar.classList.add("icon","bi","bi-arrow-repeat");

// //     let iconEliminar = document.createElement("i");
// //     iconEliminar.classList.add("icon","bi","bi-trash");
// //     iconEliminar.addEventListener("click", () => {
// //         divUsuario.remove();
// //         const Usuarios = JSON.parse(localStorage.getItem("Usuarios")) || [];
// //         for (let i = 0; i < Usuarios.length; i++) {
// //             if (Usuarios[i].cedula == cedula) {
// //                 Usuarios.splice(i, 1);
// //                 localStorage.setItem("Usuarios", JSON.stringify(Usuarios));
// //             }
// //         }
// //     });

// //     let contP = document.createElement("div");
// //     contP.className = "contP";
// //     contP.appendChild(pCedula);
// //     contP.appendChild(pNombre);
// //     contP.appendChild(pCorreo);
// //     contP.appendChild(pRol);
// //     contP.appendChild(pEstado);

// //     let contI = document.createElement("div");
// //     contI.className = "contI";
// //     contI.appendChild(iconActualizar);
// //     contI.appendChild(iconEliminar);

// //     divUsuario.appendChild(contP);
// //     divUsuario.appendChild(contI);

// //     mostrarUsuarios.appendChild(divUsuario);
// // }

// function mostrarTablaUsuarios() {
//     const Usuarios = JSON.parse(localStorage.getItem("Usuarios")) || [];
//     tbodyUsuarios.innerHTML = "";

//     Usuarios.forEach(u => {
//         let fila = document.createElement("tr");

//         fila.innerHTML = `
//             <td>${u.cedula}</td>
//             <td>${u.nombre}</td>
//             <td>${u.correo}</td>
//             <td>${u.rol}</td>
//             <td>${u.estado}</td>
//             <td>
//                 <i class="icon bi bi-arrow-repeat" style="cursor: pointer;" onclick="editarUsuario('${u.cedula}')"></i>
//                 <i class="bi bi-trash icon" style="cursor: pointer;" onclick="eliminarUsuario('${u.cedula}')"></i>
//             </td>
//         `;

//         tbodyUsuarios.appendChild(fila);
//     });
// }

// function eliminarUsuario(cedula) {
//     const mensaje = document.getElementById("mensaje");
//     mensaje.innerHTML = `
//         <span>¿Deseas eliminar este usuario con cédula <b>${cedula}</b>?</span>
//         <button id="btnSi" class="btn-crearUsuario">Sí</button>
//         <button id="btnNo" class="btn-crearUsuario">No</button>
//     `;

   
//     // Escuchar los botones
//     const btnSi = document.getElementById("btnSi");
//     const btnNo = document.getElementById("btnNo");

//     btnSi.addEventListener("click", () => {
//         const Usuarios = JSON.parse(localStorage.getItem("Usuarios")) || [];
//         const nuevos = Usuarios.filter(u => u.cedula !== cedula);
//         localStorage.setItem("Usuarios", JSON.stringify(nuevos));
//         mostrarTablaUsuarios();
//         mensaje.innerHTML = `<span style="color: #28a745;">Usuario eliminado exitosamente</span>`;
//         setTimeout(() => mensaje.innerHTML = "", 2000);
//     });

//     btnNo.addEventListener("click", () => {
//         mensaje.innerHTML = `<span style="color: #e71d73;">Eliminación cancelada</span>`;
//         setTimeout(() => mensaje.innerHTML = "", 2000);
//     });
// }

// function editarUsuario(cedulaUser) {
//     const Usuarios = JSON.parse(localStorage.getItem("Usuarios")) || [];
//     const index = Usuarios.findIndex(u => u.cedula === cedulaUser);
//     if (index === -1) return;

//     const datosUsuario = document.getElementById("datosUsuario");

//     // Rellenar el formulario con los datos del usuario seleccionado
//     datosUsuario.cedula.value = Usuarios[index].cedula;
//     datosUsuario.nombre.value = Usuarios[index].nombre;
//     datosUsuario.correo.value = Usuarios[index].correo;
//     datosUsuario.contrasena.style.display = "none";
//     datosUsuario.estado.style.display = "block";
//     datosUsuario.estado.value = Usuarios[index].estado || "Activo";
//     datosUsuario.rol.value = Usuarios[index].rol;

//     // Cambiar texto del botón a "Actualizar"
//     Crear.textContent = "Actualizar";

//     // Quitar evento de crear
//     Crear.removeEventListener("click", crearNuevoUsuario);

//     // Función para guardar los cambios
//     function guardarCambios() {
//         // Actualizar usuario en la misma posición
//         Usuarios[index].cedula = datosUsuario.cedula.value.trim();
//         Usuarios[index].nombre = datosUsuario.nombre.value.trim();
//         Usuarios[index].correo = datosUsuario.correo.value.trim();
//         Usuarios[index].estado = datosUsuario.estado.value.trim();
//         Usuarios[index].rol = datosUsuario.rol.value;

//         // Restaurar visibilidad
//         datosUsuario.contrasena.style.display = "block";
//         datosUsuario.estado.style.display = "none";

//         // Guardar en localStorage
//         localStorage.setItem("Usuarios", JSON.stringify(Usuarios));

//         // Actualizar tabla
//         mostrarTablaUsuarios();

//         // Limpiar formulario
//         datosUsuario.reset();

//         // Restaurar botón a modo crear
//         Crear.textContent = "Crear";

//         // Quitar evento de actualizar y volver al original
//         Crear.removeEventListener("click", guardarCambios);
//         Crear.addEventListener("click", crearNuevoUsuario);

//         mensaje.innerHTML = `<span style="color: #28a745;">Usuario actualizado exitosamente</span>`;
//         setTimeout(() => mensaje.innerHTML = "", 2000);
//     }

//     // Agregar evento de actualizar
//     Crear.addEventListener("click", guardarCambios);
// }


// const cerrarSesion = document.getElementById("cerrarSesion");

// cerrarSesion.addEventListener("click", () => {
//     localStorage.removeItem("usuarioLogeado");
//     localStorage.removeItem("Usuarios");
//     localStorage.removeItem("Parqueadero");
//     window.location.href = "/Frontend/html/index.html";
// });


const API_URL = "http://localhost:8080/api/usuarios";

let Crear = document.getElementById("btn-crearUsuario");
const mensaje = document.getElementById("mensaje");

let cedula = document.getElementById("cedula");
let nombre = document.getElementById("nombre");
let correo = document.getElementById("correo");
let contrasena = document.getElementById("contrasena");
let rol = document.getElementById("rol");
let estado = document.getElementById("estado");
let tbodyUsuarios = document.getElementById("tbodyUsuarios");

document.addEventListener("DOMContentLoaded", mostrarTablaUsuarios);
Crear.addEventListener("click", crearNuevoUsuario);

// ---------------------- CREAR ----------------------
async function crearNuevoUsuario() {
    if (!cedula.value || !nombre.value || !correo.value || !contrasena.value || !rol.value) {
        mensaje.innerHTML = `<span style="color:#e71d73;">Todos los campos son obligatorios</span>`;
        setTimeout(() => mensaje.innerHTML = "", 2000);
        return;
    }

    const nuevoUsuario = {
        documento: parseInt(cedula.value),
        nombre: nombre.value.trim(),
        correo: correo.value.trim(),
        contrasena: contrasena.value.trim(),
        rol: rol.value.trim(),
        estado: "Activo"
    };

    try {
        const res = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(nuevoUsuario)
        });

        const text = await res.text();
        mensaje.innerHTML = `<span style="color:${res.ok ? '#28a745' : '#e71d73'};">${text}</span>`;
        setTimeout(() => mensaje.innerHTML = "", 2000);
        if (res.ok) {
            limpiarFormulario();
            mostrarTablaUsuarios();
        }
    } catch (err) {
        mensaje.innerHTML = `<span style="color:#e71d73;">Error: ${err.message}</span>`;
    }
}

// ---------------------- LISTAR ----------------------
async function mostrarTablaUsuarios() {
    try {
        const res = await fetch(API_URL);
        const usuarios = await res.json();

        tbodyUsuarios.innerHTML = "";
        usuarios.forEach(u => {
            const fila = document.createElement("tr");
            fila.innerHTML = `
                <td>${u.documento}</td>
                <td>${u.nombre}</td>
                <td>${u.correo}</td>
                <td>${u.rol}</td>
                <td>${u.estado}</td>
                <td>
                    <i class="bi bi-arrow-repeat icon" style="cursor:pointer;" onclick="editarUsuario(${u.documento})"></i>
                    <i class="bi bi-trash icon" style="cursor:pointer;" onclick="eliminarUsuario(${u.documento})"></i>
                </td>
            `;
            tbodyUsuarios.appendChild(fila);
        });
    } catch (err) {
        console.error(err);
        mensaje.innerHTML = `<span style="color:#e71d73;">Error al cargar usuarios</span>`;
    }
}

// ---------------------- ELIMINAR ----------------------
async function eliminarUsuario(documento) {
    mensaje.innerHTML = `
        <span>¿Eliminar usuario <b>${documento}</b>?</span>
        <button id="btnSi" class="btn-crearUsuario">Sí</button>
        <button id="btnNo" class="btn-crearUsuario">No</button>
    `;

    document.getElementById("btnSi").addEventListener("click", async () => {
        try {
            const res = await fetch(`${API_URL}/${documento}`, { method: "DELETE" });
            const text = await res.text();
            mensaje.innerHTML = `<span style="color:${res.ok ? '#28a745' : '#e71d73'};">${text}</span>`;
            if (res.ok) mostrarTablaUsuarios();
        } catch (err) {
            mensaje.innerHTML = `<span style="color:#e71d73;">Error: ${err.message}</span>`;
        } finally {
            setTimeout(() => mensaje.innerHTML = "", 2000);
        }
    });

    document.getElementById("btnNo").addEventListener("click", () => {
        mensaje.innerHTML = `<span style="color:#e71d73;">Cancelado</span>`;
        setTimeout(() => mensaje.innerHTML = "", 2000);
    });
}

// ---------------------- EDITAR ----------------------
async function editarUsuario(documento) {
    try {
        const res = await fetch(`${API_URL}/${documento}`);
        const user = await res.json();

        cedula.value = user.documento;
        nombre.value = user.nombre;
        correo.value = user.correo;
        contrasena.style.display = "none";
        estado.style.display = "block";
        estado.value = user.estado;
        rol.value = user.rol;

        Crear.textContent = "Actualizar";
        Crear.removeEventListener("click", crearNuevoUsuario);
        Crear.addEventListener("click", async function guardarCambios() {
            const actualizado = {
                documento: parseInt(cedula.value),
                nombre: nombre.value.trim(),
                correo: correo.value.trim(),
                contrasena: user.contrasena,
                estado: estado.value.trim(),
                rol: rol.value.trim()
            };

            try {
                const res = await fetch(`${API_URL}/${documento}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(actualizado)
                });
                const text = await res.text();
                mensaje.innerHTML = `<span style="color:${res.ok ? '#28a745' : '#e71d73'};">${text}</span>`;
                if (res.ok) mostrarTablaUsuarios(); limpiarFormulario();
            } catch (err) {
                mensaje.innerHTML = `<span style="color:#e71d73;">Error: ${err.message}</span>`;
            } finally {
                setTimeout(() => mensaje.innerHTML = "", 2000);
                contrasena.style.display = "block";
                estado.style.display = "none";
                Crear.textContent = "Crear";
                Crear.removeEventListener("click", guardarCambios);
                Crear.addEventListener("click", crearNuevoUsuario);
                limpiarFormulario();
            }
        });
    } catch (err) {
        mensaje.innerHTML = `<span style="color:#e71d73;">Error al cargar usuario</span>`;
    }
}

// ---------------------- UTILIDAD ----------------------
function limpiarFormulario() {
    cedula.value = "";
    nombre.value = "";
    correo.value = "";
    contrasena.value = "";
    rol.value = "";
    if (estado) estado.value = "";
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