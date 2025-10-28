package com.cesde.main;

import com.cesde.conexion.Conexion;
import com.cesde.server.Servidor;
import com.cesde.control.*;
import com.cesde.model.parqueadero.Parqueadero;

public class CesdePS {

    public static void main(String[] args) {
        // Crear instancias de los controles
//        Parqueadero parqueadero = new Parqueadero(5); // 5 espacios de ejemplo
//        UsuarioControl uControl = new UsuarioControl();
//        VehiculoControl vControl = new VehiculoControl(uControl);
//        OcupacionControl oControl = new OcupacionControl();
//        ParqueaderoControl pControl = new ParqueaderoControl(parqueadero, vControl, oControl);

        // Inicia el sistema desde Login
//        Login login = new Login(uControl, pControl, vControl, oControl);
//        login.iniciarSesion();
        new Thread(Servidor::iniciarServidor).start();
    }
}
