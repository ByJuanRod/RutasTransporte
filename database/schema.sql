CREATE DATABASE rutas_transporte_db;
USE rutas_transporte_db;

CREATE TABLE Paradas(
                        codigo INT AUTO_INCREMENT PRIMARY KEY,
                        nombre_parada VARCHAR(40) NOT NULL,
                        tipo_parada VARCHAR(30) NOT NULL,
                        ubicacion VARCHAR(60) NOT NULL
);

CREATE TABLE Rutas(
                      codigo INT AUTO_INCREMENT PRIMARY KEY,
                      nombre_ruta VARCHAR(80) NOT NULL,
                      distancia INT NOT NULL,
                      costo FLOAT NOT NULL,
                      tiempo INT NOT NULL,
                      trasbordos INT NOT NULL,
                      origen INT,
                      destino INT,
                      FOREIGN KEY (origen) REFERENCES Paradas(codigo) ON DELETE CASCADE,
                      FOREIGN KEY (destino) REFERENCES Paradas(codigo) ON DELETE CASCADE
);

CREATE TABLE Eventos(
                        codigo INT AUTO_INCREMENT PRIMARY KEY,
                        ruta INT NOT NULL,
                        tipo_evento VARCHAR(30) NOT NULL,
                        fecha_inicio DATETIME NOT NULL,
                        fecha_fin DATETIME,
                        ADD UNIQUE KEY uq_ruta_activa (ruta),
                        FOREIGN KEY (ruta) REFERENCES Rutas(codigo) ON DELETE CASCADE

);
