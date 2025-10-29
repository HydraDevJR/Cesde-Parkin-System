// document.addEventListener("DOMContentLoaded", function () {
//     if(!localStorage.getItem("Usuarios") && !localStorage.getItem("Parqueadero")){
//         const Usuarios = [
//             {cedula: "1", nombre: "adminDefault", correo: "admin@cesde.com", contrasena: "1234", estado: "Activo", rol: "Administrador"},
//             {cedula: "2", nombre: "estDefault", correo: "est@cesde.com", contrasena: "1234", estado: "Activo", rol: "Estudiante"}
//         ];
//         localStorage.setItem("Usuarios", JSON.stringify(Usuarios));

//         const Parqueadero = [
//             {idParqueadero: "1", nomParqueadero: "CESDE Parking System", dirParqueadero: "Carrera 42 #48-20", tarifaMoto: 1500, tarifaCarro: 3000}
//         ];
//         localStorage.setItem("Parqueadero", JSON.stringify(Parqueadero));
//     }

// const form = document.getElementById("inputs");

// form.addEventListener("submit", (event) => {
//     event.preventDefault();

//     localStorage.removeItem("usuarioLogeado");

//     const email = document.getElementById("email").value;
//     const password = document.getElementById("contrasena").value;

//     const Usuarios = JSON.parse(localStorage.getItem("Usuarios")) || [];
//     const validarMail = Usuarios.find((user) => user.correo === email && user.contrasena === password);

//     const shadowLogin = this.getElementById("login");

//     if (validarMail) {
//         const mensaje = document.getElementById("mensaje");
//         mensaje.textContent = `Bienvenido ${validarMail.nombre}`;
//         mensaje.style.color = "green";

//         shadowLogin.style.boxShadow = "0px 0px 500px green";

//         localStorage.setItem("usuarioLogeado", JSON.stringify(validarMail));
        
//         if(validarMail.rol == "Administrador"){
//             setTimeout(function(){
//                 window.location.href = "/Frontend/html/Administrativo/administrativo.html";
//             },800);
//         }else{
//             setTimeout(function(){
//                 window.location.href = "/Frontend/html/Usuarios/panel_usuario.html";
//             },800);
//         }

//     } else {
//         const mensaje = document.getElementById("mensaje");
//         mensaje.textContent = "Correo o contraseña incorrecta";
//         mensaje.style.color = "var(--rosado)";
//         shadowLogin.style.boxShadow = "0px 0px 500px var(--rosado)";
        
//     }

// });

// });
const API_URL = "http://localhost:8080/api/login";

document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("inputs");
    const mensaje = document.getElementById("mensaje");
    const shadowLogin = document.getElementById("login");

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        // Limpiar sesión anterior
        sessionStorage.removeItem("usuarioLogeado");

        const email = document.getElementById("email").value.trim();
        const password = document.getElementById("contrasena").value.trim();

        // Validación básica
        if (!email || !password) {
            mostrarError("Por favor complete todos los campos");
            return;
        }

        // Deshabilitar botón durante la petición
        const botonSubmit = form.querySelector("button");
        botonSubmit.disabled = true;
        botonSubmit.textContent = "Validando...";

        try {
            const response = await fetch(API_URL, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    correo: email,
                    contrasena: password
                })
            });

            if (response.ok) {
                const usuario = await response.json();
                
                // Login exitoso
                mensaje.textContent = `¡Bienvenido ${usuario.nombre}!`;
                mensaje.style.color = "green";
                shadowLogin.style.boxShadow = "0px 0px 500px green";

                // Guardar usuario en sessionStorage (más seguro que localStorage para sesiones)
                sessionStorage.setItem("usuarioLogeado", JSON.stringify(usuario));

                // Redirigir según el rol
                setTimeout(() => {
                    if (usuario.rol === "Administrador") {
                        window.location.href = "/Frontend/html/Administrativo/administrativo.html";
                    } else if (usuario.rol === "Estudiante") {
                        window.location.href = "/Frontend/html/Usuarios/panel_usuario.html";
                    } else {
                        mostrarError("Rol no reconocido");
                    }
                }, 800);

            } else {
                // Error de autenticación
                const errorText = await response.text();
                mostrarError(errorText);
            }

        } catch (error) {
            console.error("Error en login:", error);
            mostrarError("Error de conexión con el servidor. Verifique que el backend esté ejecutándose.");
        } finally {
            // Rehabilitar botón
            botonSubmit.disabled = false;
            botonSubmit.textContent = "INICIAR SESION";
        }
    });

    /**
     * Muestra un mensaje de error con estilo
     */
    function mostrarError(textoError) {
        mensaje.textContent = textoError;
        mensaje.style.color = "var(--rosado)";
        shadowLogin.style.boxShadow = "0px 0px 500px var(--rosado)";
        
        // Limpiar mensaje después de 3 segundos
        setTimeout(() => {
            mensaje.textContent = "Ingresa tus datos";
            mensaje.style.color = "";
            shadowLogin.style.boxShadow = "";
        }, 2000);
    }
});