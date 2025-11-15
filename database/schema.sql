create database rutas_transporte_db
use rutas_transporte_db

create table Paradas(
                        codigo INT AUTO_INCREMENT PRIMARY KEY,
                        nombre_parada VARCHAR(40) NOT NULL,
                        tipo_parada ENUM('bus','metro','tren') NOT NULL,
                        ubicacion VARCHAR(60) NOT NULL
)

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