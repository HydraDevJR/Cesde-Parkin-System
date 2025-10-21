CREATE DATABASE CESDE_Parking_System;

USE CESDE_Parking_System;

CREATE TABLE Usuarios (
    documento INT PRIMARY KEY,
    nombre VARCHAR(100),
    correo VARCHAR(100),
    contrasena VARCHAR(100),
    rol VARCHAR(100),
    estado VARCHAR(50),
    fechaCreacion DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Estudiantes (
    documento INT PRIMARY KEY,
    carrera VARCHAR(100),
    semestre INT,
    FOREIGN KEY (documento) REFERENCES Usuarios(documento)
);

CREATE TABLE Administradores (
    documento INT PRIMARY KEY,
    area VARCHAR(100),
    cargo VARCHAR(100),
    FOREIGN KEY (documento) REFERENCES Usuarios(documento)
);

CREATE TABLE Vehiculos (
    placa VARCHAR(10) PRIMARY KEY,
    tipo VARCHAR(50),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    color VARCHAR(30),
    documento INT,
    fechaRegistro DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (documento) REFERENCES Usuarios(documento)
);

CREATE TABLE Parqueaderos (
    idParqueadero INT PRIMARY KEY,
    nombre VARCHAR(100),
    ubicacion VARCHAR(200),
    capacidad INT,
    tarifa_carro DECIMAL(10,2),
    tarifa_moto DECIMAL(10,2)
);

CREATE TABLE Espacios (
    numero INT PRIMARY KEY,
    estado VARCHAR(50), -- 'ocupado', 'libre', 'mantenimiento', etc.
    tipo VARCHAR(50), -- 'carro', 'moto', 'discapacitado', etc.
    idParqueadero INT,
    FOREIGN KEY (idParqueadero) REFERENCES Parqueaderos(idParqueadero)
);

CREATE TABLE Ocupacion (
    id_ocupacion INT PRIMARY KEY IDENTITY(1,1),
    id_vehiculo VARCHAR(10), -- Es la placa del vehículo
    id_espacio INT,
    horaEntrada DATETIME DEFAULT CURRENT_TIMESTAMP,
    horaSalida DATETIME,
    estado VARCHAR(20), -- "ACTIVO" o "FINALIZADO"
    tarifa DECIMAL(10,2),
    FOREIGN KEY (id_vehiculo) REFERENCES Vehiculos(placa),
    FOREIGN KEY (id_espacio) REFERENCES Espacios(numero)
);

CREATE TABLE Reservas (
    id_reserva INT PRIMARY KEY IDENTITY(1,1),
    documento INT,
    placa_vehiculo VARCHAR(10),
    numero_espacio INT,
    fecha_reserva DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio DATETIME,
    fecha_fin DATETIME,
    estado VARCHAR(20), -- 'PENDIENTE', 'ACTIVA', 'FINALIZADA', 'CANCELADA'
    observaciones VARCHAR(200),
    FOREIGN KEY (documento) REFERENCES Usuarios(documento),
    FOREIGN KEY (placa_vehiculo) REFERENCES Vehiculos(placa),
    FOREIGN KEY (numero_espacio) REFERENCES Espacios(numero)
);

CREATE TABLE Tickets (
    id_recibo INT PRIMARY KEY IDENTITY(1,1),
    id_ocupacion INT,
    placa_vehiculo VARCHAR(10),
    numero_espacio INT,
    hora_entrada DATETIME DEFAULT CURRENT_TIMESTAMP,
    hora_salida DATETIME,
    tarifa_hora DECIMAL(10,2),
    total_pagar DECIMAL(10,2),
    pagado BIT DEFAULT 0,
    FOREIGN KEY (placa_vehiculo) REFERENCES Vehiculos(placa),
    FOREIGN KEY (numero_espacio) REFERENCES Espacios(numero),
    FOREIGN KEY (id_ocupacion) REFERENCES Ocupacion(id_ocupacion)
);
