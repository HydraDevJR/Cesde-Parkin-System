document.addEventListener("DOMContentLoaded", function () {
    if(!localStorage.getItem("Usuarios")){
    const Usuarios = [
        {cedula: "1", nombre: "adminDefault", correo: "admin@cesde.com", contrasena: "1234", rol: "administrador"},
        {cedula: "2", nombre: "estDefault", correo: "est@cesde.com", contrasena: "1234", rol: "estudiante"}
    ];
    localStorage.setItem("Usuarios", JSON.stringify(Usuarios));
}

const form = document.getElementById("inputs");

form.addEventListener("submit", (event) => {
    event.preventDefault();

    localStorage.removeItem("usuarioLogeado");

    const email = document.getElementById("email").value;
    const password = document.getElementById("contrasena").value;

    const Usuarios = JSON.parse(localStorage.getItem("Usuarios")) || [];
    const validarMail = Usuarios.find((user) => user.correo === email && user.contrasena === password);


    if (validarMail) {
        alert("Login exitoso");
        localStorage.setItem("usuarioLogeado", JSON.stringify(validarMail));
        
        if(validarMail.rol == "administrador"){
            window.location.href = "/Frontend/html/Administrativo/administrativo.html";
        }else{
            window.location.href = "/Frontend/html/Usuarios/panel_usuario.html";
        }

    } else {
        alert("Credenciales inválidas");
    }

});

});