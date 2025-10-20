package com.control.model;
import com.usuarios.model.Administrador;
import com.usuarios.model.Estudiante;
import com.usuarios.model.Usuario;

import java.util.Scanner;
//La clase Login maneja toda l
public class Login {
    private final UsuarioControl uControl;
    private final ParqueaderoControl pControl;
    private final VehiculoControl vControl;
    private final OcupacionControl oControl;
    private final Scanner read;

    public Login(UsuarioControl uControl, ParqueaderoControl pControl, VehiculoControl vControl, OcupacionControl oControl) {
        this.uControl = uControl;
        this.pControl = pControl;
        this.vControl = vControl;
        this.oControl = oControl;
        this.read = new Scanner(System.in);
    }

    // Inicia sesión y redirige según el tipo de usuario
    public void iniciarSesion() {
        System.out.println("*** Bienvenido a CESDE Parkys System ***");
        System.out.println("Ingresa los siguientes datos para iniciar sesion: ");
        System.out.print("Correo: ");
        String correo = read.next();
        System.out.print("Contraseña: ");
        String contrasena = read.next();

        Usuario usuario = uControl.login(correo, contrasena);

        if (usuario == null) {
            System.out.println("Correo o contraseña incorrectos.");
            return;
        }
        //El operador instanceof me ayuda a determinar
        // si el objeto usuario es instancia de Administrador/Estudiante
        // si es correcto automáticamente crea una variable de tipo Administrador/Estudiante
        // ya casteada, siendo mas seguro, mas limpio y mas legible
        if (usuario instanceof Administrador admin) {
            System.out.println("\n¡Bienvenido  " + admin.getNombre() + "!");
            adminMenu(admin);
        } else if (usuario instanceof Estudiante est) {
            System.out.println("\n¡Bienvenido  " + est.getNombre() + "!");
            estudianteMenu(est);
        }
    }

    // Menú de administrador
    private void adminMenu(Administrador admin) {
        int option;
        do {
            System.out.println("\n*** Menú ***");
            System.out.println("1. Gestionar usuarios");
            System.out.println("2. Gestionar vehículos");
            System.out.println("3. Gestionar parqueadero");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            option = read.nextInt();

            switch (option) {
                case 1 -> uControl.menuUsuario(admin);
                case 2 -> vControl.menuVehiculoAdmin(); // Menú global
                case 3 -> pControl.menuParqueadero();
                case 4 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción incorrecta, intente nuevamente.");
            }
        } while (option != 4);
    }
    //Menu estudiantes
    public void estudianteMenu(Estudiante est) {
        int option;
        do {
            System.out.println("\n*** CESDE PARKING SYSTEM - Estudiante: " + est.getNombre() + " ***");
            System.out.println("1. Gestión de Perfil");
            System.out.println("2. Gestión de Vehículos");
            System.out.println("3. Salir del Sistema");
            System.out.print("Seleccione una opción: ");

            option = read.nextInt();

            switch (option) {
                case 1 -> uControl.menuUsuario(est);
                case 2 -> vControl.menuVehiculo(est);
                case 3 -> System.out.println("Gracias por usar nuestro sistema!");
                default -> System.out.println("Opción incorrecta, intente nuevamente.");
            }
        } while (option != 3);
    }
}
