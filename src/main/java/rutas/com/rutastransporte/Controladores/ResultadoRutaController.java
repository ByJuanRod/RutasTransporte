package rutas.com.rutastransporte.Controladores;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import rutas.com.rutastransporte.Modelos.Criterio;
import rutas.com.rutastransporte.Modelos.RutaPosible;
import rutas.com.rutastransporte.Recursos;

public class ResultadoRutaController {
    private RutaPosible rutaPosible;

    public void setRutaPosible(RutaPosible ruta) {
        rutaPosible = ruta;
    }

    @FXML
    private ImageView imgCondicion;

    @FXML
    private Label lblIndicador;

    @FXML
    private Label lblCosto;

    @FXML
    private Label lblDistancia;

    @FXML
    private Label lblTiempoEstimado;

    @FXML
    private Label lblCantTrasbordos;

    @FXML
    private Button btnVerDetalles;

    public AnchorPane crearInterfaz() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefHeight(267.0);
        anchorPane.setPrefWidth(794.0);
        anchorPane.setStyle("-fx-background-color: #1B4B6B; -fx-background-radius: 10px;");

        Circle circle = new Circle();
        circle.setLayoutX(50.0);
        circle.setLayoutY(50.0);
        circle.setRadius(36.0);
        circle.setFill(javafx.scene.paint.Color.valueOf("#19173c"));
        circle.setStroke(javafx.scene.paint.Color.WHITE);
        circle.setStrokeType(javafx.scene.shape.StrokeType.INSIDE);
        AnchorPane.setLeftAnchor(circle, 14.0);
        AnchorPane.setTopAnchor(circle, 14.0);

        imgCondicion = new ImageView();
        imgCondicion.setFitHeight(53.0);
        imgCondicion.setFitWidth(67.0);
        imgCondicion.setLayoutX(24.0);
        imgCondicion.setLayoutY(24.0);
        imgCondicion.setPickOnBounds(true);
        imgCondicion.setPreserveRatio(true);
        AnchorPane.setLeftAnchor(imgCondicion, 24.0);
        AnchorPane.setTopAnchor(imgCondicion, 24.0);

        try {
            Image image = new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/ahorro.png"));
            imgCondicion.setImage(image);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
        }

        lblIndicador = new Label();
        lblIndicador.setLayoutX(101.0);
        lblIndicador.setLayoutY(33.0);
        lblIndicador.setPrefHeight(34.0);
        lblIndicador.setPrefWidth(386.0);
        lblIndicador.setText("Ruta Más Económica");
        lblIndicador.setTextFill(javafx.scene.paint.Color.WHITE);
        AnchorPane.setLeftAnchor(lblIndicador, 101.0);
        Font indicadorFont = Font.font("Century Gothic Bold", 27.0);
        lblIndicador.setFont(indicadorFont);

        Line line = new Line();
        line.setLayoutX(201.0);
        line.setLayoutY(67.0);
        line.setEndX(286.20001220703125);
        line.setEndY(-1.52587890625E-5);
        line.setStartX(-100.0);
        line.setStroke(javafx.scene.paint.Color.WHITE);
        line.setStrokeLineCap(javafx.scene.shape.StrokeLineCap.BUTT);
        line.setStrokeLineJoin(javafx.scene.shape.StrokeLineJoin.ROUND);
        line.setStrokeWidth(2.0);

        Label lblCostoTitulo = crearLabelTitulo("Costo:", 24.0, 105.0);
        Label lblDistanciaTitulo = crearLabelTitulo("Distancia:", 25.0, 142.0);
        Label lblTrasbordosTitulo = crearLabelTitulo("Cantidad de Trasbordos:", 24.0, 223.0);
        Label lblTiempoTitulo = crearLabelTitulo("Tiempo Estimado:", 24.0, 182.0);

        btnVerDetalles = new Button();
        btnVerDetalles.setLayoutX(616.0);
        btnVerDetalles.setLayoutY(204.0);
        btnVerDetalles.setPrefHeight(43.0);
        btnVerDetalles.setPrefWidth(153.0);
        btnVerDetalles.setStyle("-fx-background-color: #49808E; -fx-border-color: WHITE; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        btnVerDetalles.setText("Ver Detalles");
        btnVerDetalles.setTextFill(javafx.scene.paint.Color.WHITE);
        btnVerDetalles.setDefaultButton(true);
        AnchorPane.setBottomAnchor(btnVerDetalles, 20.0);
        AnchorPane.setRightAnchor(btnVerDetalles, 24.0);
        Font buttonFont = Font.font("Century Gothic Bold", 17.0);
        btnVerDetalles.setFont(buttonFont);

        btnVerDetalles.setOnAction(this::verDetallesClick);

        lblCosto = crearLabelValor("$400", 86.0, 105.0);
        AnchorPane.setLeftAnchor(lblCosto, 86.0);

        lblDistancia = crearLabelValor("500 km", 109.0, 142.0);
        AnchorPane.setLeftAnchor(lblDistancia, 109.0);

        lblTiempoEstimado = crearLabelValor("1h 10m", 165.0, 182.0);

        lblCantTrasbordos = crearLabelValor("5", 223.0, 223.0);
        AnchorPane.setLeftAnchor(lblCantTrasbordos, 223.0);

        anchorPane.getChildren().addAll(
                circle, imgCondicion, lblIndicador, line,
                lblCostoTitulo, lblDistanciaTitulo, lblTrasbordosTitulo, lblTiempoTitulo,
                btnVerDetalles, lblCosto, lblDistancia, lblTiempoEstimado, lblCantTrasbordos
        );

        return anchorPane;
    }

    private Label crearLabelTitulo(String texto, double x, double y) {
        Label label = new Label();
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setText(texto);
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        Font font = Font.font("Century Gothic", 16.0);
        label.setFont(font);
        AnchorPane.setLeftAnchor(label, x);
        return label;
    }

    private Label crearLabelValor(String texto, double x, double y) {
        Label label = new Label();
        label.setLayoutX(x);
        label.setLayoutY(y);
        label.setPrefHeight(20.0);
        label.setPrefWidth(322.0);
        label.setText(texto);
        label.setTextFill(javafx.scene.paint.Color.WHITE);
        Font font = Font.font("Century Gothic Bold", 16.0);
        label.setFont(font);
        return label;
    }

    @FXML
    public void initialize(){
        cargarDatos();
    }

    private void cargarDatos(){
        if (rutaPosible != null) {
            lblCantTrasbordos.setText(String.valueOf(rutaPosible.getCantTrasbordos()));
            lblCosto.setText("$" + rutaPosible.getCostoTotal());
            lblTiempoEstimado.setText(rutaPosible.getTiempoFormatado());
            lblDistancia.setText(rutaPosible.getDistanciaFormatado());
            lblIndicador.setText(rutaPosible.getCriterio().getNombre());
            try {
                Image img = new Image(getClass().getResourceAsStream("/rutas/com/rutastransporte/imagenes/" + rutaPosible.getCriterio().getImagen()));
                imgCondicion.setImage(img);
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen del criterio: " + e.getMessage());
            }
        }
    }

    public void verDetallesClick(ActionEvent e){
        // Implementar la lógica para ver detalles
    }
}