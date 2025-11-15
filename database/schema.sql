/* PASO 0: Borra la base de datos si ya existe (para empezar de cero)
*/
DROP DATABASE IF EXISTS rutas_transporte_db;

/* PASO 1: Crear la base de datos
*/
CREATE DATABASE rutas_transporte_db;

/* PASO 2: Usarla
*/
USE rutas_transporte_db;

/* PASO 3: Crear la tabla de Paradas (basada en Parada.java)
*/
CREATE TABLE Paradas (
                         codigo VARCHAR(50) PRIMARY KEY,
                         nombreParada VARCHAR(255) NOT NULL,
                         tipo VARCHAR(50),
                         ubicacion VARCHAR(255)
);

/* PASO 4: Crear la tabla de Rutas (basada en Ruta.java)
*/
CREATE TABLE Rutas (
                       codigo VARCHAR(50) PRIMARY KEY,
                       nombre VARCHAR(255),
                       origen_codigo VARCHAR(50) NOT NULL,
                       destino_codigo VARCHAR(50) NOT NULL,
                       distancia INT NOT NULL,
                       costo FLOAT NOT NULL,
                       tiempo INT NOT NULL,
                       trasbordos INT NOT NULL,
                       tieneEvento BOOLEAN DEFAULT false,
                       eventoActual VARCHAR(50) DEFAULT 'NORMAL',

                       CONSTRAINT fk_parada_origen
                           FOREIGN KEY (origen_codigo) REFERENCES Paradas(codigo),

                       CONSTRAINT fk_parada_destino
                           FOREIGN KEY (destino_codigo) REFERENCES Paradas(codigo)
);

CREATE TABLE Eventos (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         ruta_codigo VARCHAR(50) NOT NULL,
                         tipoEvento VARCHAR(50) NOT NULL,
                         fechaInicio DATETIME NOT NULL,
                         fechaFin DATETIME,
                         activo BOOLEAN DEFAULT true,

                         CONSTRAINT fk_ruta_evento
                             FOREIGN KEY (ruta_codigo) REFERENCES Rutas(codigo)
);