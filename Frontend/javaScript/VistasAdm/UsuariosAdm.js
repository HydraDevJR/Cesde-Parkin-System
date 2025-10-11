let Crear = document.getElementById("btn-crearUsuario");
let mostrarUsuarios = document.getElementById("mostrarUsuarios");

let cedula = document.getElementById("cedula");
let nombre = document.getElementById("nombre");
let correo = document.getElementById("correo");
let contrasena = document.getElementById("contrasena");
let rol = document.getElementById("rol");

Crear.addEventListener("click", () => {
    agregarUsuario(cedula.value, nombre.value, correo.value, rol.value);
});

function agregarUsuario(cedula, nombre, correo, rol) {

    let divUsuario = document.createElement("div");

    divUsuario.id = "usuarioItem";

    let pCedula = document.createElement("p");
    pCedula.textContent = cedula;

    let pNombre = document.createElement("p");
    pNombre.textContent = nombre;

    let pCorreo = document.createElement("p");
    pCorreo.textContent = correo;

    let pRol = document.createElement("p");
    pRol.textContent = rol;

    let pEstado = document.createElement("p");
    pEstado.textContent = "Activo";

    const iconActualizar = document.createElement("i");
    iconActualizar.classList.add("icon","bi","bi-arrow-repeat");

    let iconEliminar = document.createElement("i");
    iconEliminar.classList.add("icon","bi","bi-trash");
    iconEliminar.addEventListener("click", () => {
        divUsuario.remove();
    });

    let contP = document.createElement("div");
    contP.className = "contP";
    contP.appendChild(pCedula);
    contP.appendChild(pNombre);
    contP.appendChild(pCorreo);
    contP.appendChild(pRol);
    contP.appendChild(pEstado);

    let contI = document.createElement("div");
    contI.className = "contI";
    contI.appendChild(iconActualizar);
    contI.appendChild(iconEliminar);

    divUsuario.appendChild(contP);
    divUsuario.appendChild(contI);

    mostrarUsuarios.appendChild(divUsuario);
}