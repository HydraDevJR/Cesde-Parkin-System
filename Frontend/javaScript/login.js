document.addEventListener("DOMContentLoaded", function () {
    if(!localStorage.getItem("Usuarios") && !localStorage.getItem("Parqueadero")){
        const Usuarios = [
            {cedula: "1", nombre: "adminDefault", correo: "admin@cesde.com", contrasena: "1234", estado: "Activo", rol: "Administrador"},
            {cedula: "2", nombre: "estDefault", correo: "est@cesde.com", contrasena: "1234", estado: "Activo", rol: "Estudiante"}
        ];
        localStorage.setItem("Usuarios", JSON.stringify(Usuarios));

        const Parqueadero = [
            {idParqueadero: "1", nomParqueadero: "CESDE Parking System", dirParqueadero: "Carrera 42 #48-20", tarifaMoto: 1500, tarifaCarro: 3000}
        ];
        localStorage.setItem("Parqueadero", JSON.stringify(Parqueadero));
    }

const form = document.getElementById("inputs");

form.addEventListener("submit", (event) => {
    event.preventDefault();

    localStorage.removeItem("usuarioLogeado");

    const email = document.getElementById("email").value;
    const password = document.getElementById("contrasena").value;

    const Usuarios = JSON.parse(localStorage.getItem("Usuarios")) || [];
    const validarMail = Usuarios.find((user) => user.correo === email && user.contrasena === password);

    const shadowLogin = this.getElementById("login");

    if (validarMail) {
        const mensaje = document.getElementById("mensaje");
        mensaje.textContent = `Bienvenido ${validarMail.nombre}`;
        mensaje.style.color = "green";

        shadowLogin.style.boxShadow = "0px 0px 500px green";

        localStorage.setItem("usuarioLogeado", JSON.stringify(validarMail));
        
        if(validarMail.rol == "Administrador"){
            setTimeout(function(){
                window.location.href = "/Frontend/html/Administrativo/administrativo.html";
            },800);
        }else{
            setTimeout(function(){
                window.location.href = "/Frontend/html/Usuarios/panel_usuario.html";
            },800);
        }

    } else {
        const mensaje = document.getElementById("mensaje");
        mensaje.textContent = "Correo o contraseña incorrecta";
        mensaje.style.color = "var(--rosado)";
        shadowLogin.style.boxShadow = "0px 0px 500px var(--rosado)";
        
    }

});

});