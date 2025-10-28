const cerrarSesion = document.getElementById("cerrarSesion");

cerrarSesion.addEventListener("click", () => {
    localStorage.removeItem("usuarioLogeado");
    localStorage.removeItem("Usuarios");
    localStorage.removeItem("Parqueadero");
    window.location.href = "/Frontend/html/index.html";
});