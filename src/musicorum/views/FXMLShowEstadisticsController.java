/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Alumno;
import WS.Ejercicio;
import WS.Estadistica;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author kevin
 */
public class FXMLShowEstadisticsController extends BaseController implements Initializable {

    @FXML
    private Label nombre;
    @FXML
    private Label unidad;
    @FXML
    private Label total;
    @FXML
    private Label aciertos;
    @FXML
    private LineChart<String,Number> lineChart;
    private Ejercicio ejercicio;
    private Alumno alumno;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    @FXML
    public void cancelar(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    public void set(Ejercicio ejercicio,Alumno alumno){
        this.ejercicio = ejercicio;
        this.alumno = alumno;
        setData();
    }
    private void setData(){
        nombre.setText(ejercicio.getNombre());
        unidad.setText(this.port.getUnidadById(ejercicio.getUnidadId()).getNombre());
        total.setText(this.port.getEjercicioTotal(ejercicio, alumno)+"");
        aciertos.setText(this.port.getEjercicioExitos(ejercicio, alumno)+"");
        List<Estadistica> estadisticas = this.port.getEstadisticas(alumno.getId(), ejercicio.getId());
        XYChart.Series exitos = new XYChart.Series<>();
        XYChart.Series fallos = new XYChart.Series<>();
        exitos.setName("Exitos");
        fallos.setName("Errores");
        estadisticas.forEach(x->{
            if(x.isExito())
                exitos.getData().add(new XYChart.Data(x.getFecha(),x.getNum()));
            else
                fallos.getData().add(new XYChart.Data(x.getFecha(),x.getNum()));
        });
        lineChart.getData().addAll(exitos,fallos);
    }
    
}
