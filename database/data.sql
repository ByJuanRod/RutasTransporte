USE rutas_transporte_db;

INSERT INTO Paradas (nombre_parada, tipo_parada, ubicacion)
VALUES
    ('Parada Central', 'BUS', 'Av. 27 de Febrero esq. Estrella Sadhalá'),
    ('Estación Principal', 'TREN', 'Av. Juan Pablo Duarte');

INSERT INTO Rutas (nombre_ruta, origen, destino, distancia, costo, tiempo, trasbordos)
VALUES
    ('Ruta Expreso 1', 1, 2, 2500, 50.0, 15, 0);