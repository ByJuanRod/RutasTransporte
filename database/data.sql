USE rutas_transporte_db;

/* 1. Insertar Paradas de ejemplo
*/
INSERT INTO Paradas (codigo, nombreParada, tipo, ubicacion)
VALUES
    ('P001', 'Parada Central', 'BUS', 'Av. 27 de Febrero esq. Estrella Sadhalá'),
    ('P002', 'Estación Principal', 'TREN', 'Av. Juan Pablo Duarte');

/* 2. Insertar Rutas que conecten esas paradas
*/
INSERT INTO Rutas (codigo, nombre, origen_codigo, destino_codigo, distancia, costo, tiempo, trasbordos)
VALUES
    ('R01', 'Ruta Expreso 1', 'P001', 'P002', 2500, 50.0, 15, 0);