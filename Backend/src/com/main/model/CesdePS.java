package com.main.model;

import com.control.model.*;
import com.parqueadero.model.Parqueadero;

public class CesdePS {

    public static void main(String[] args) {
        // Crear instancias de los controles
        Parqueadero parqueadero = new Parqueadero(3); // 3 espacios de ejemplo
        UsuarioControl uControl = new UsuarioControl();
        VehiculoControl vControl = new VehiculoControl(uControl);
        OcupacionControl oControl = new OcupacionControl();
        ParqueaderoControl pControl = new ParqueaderoControl(parqueadero, vControl, oControl);

        // Inicia el sistema desde Login
        Login login = new Login(uControl, pControl, vControl, oControl);
        login.iniciarSesion();
    }
}
