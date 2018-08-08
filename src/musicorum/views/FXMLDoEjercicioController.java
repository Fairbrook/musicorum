/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Alumno;
import WS.Ejercicio;
import WS.EjerciciosPorAlumno;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import musicorum.AlumnoConverter;

/**
 * FXML Controller class
 *
 * @author kevin
 */
public class FXMLDoEjercicioController extends BaseController implements Initializable {

    @FXML
    private ChoiceBox comboAlumnos;
    private Ejercicio ejercicio;
    private ObservableList observableList;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Alumno> alumnos = this.port.getAlumnos(this.getParameter().getId());
        List<AlumnoConverter> alumnosCombo = new ArrayList<>();
        alumnos.forEach(x->alumnosCombo.add(new AlumnoConverter(x)));
        observableList = FXCollections.observableArrayList();
        observableList.add(0,"Seleccionar");
        observableList.add(new Separator());
        observableList.addAll(alumnosCombo);
        comboAlumnos.setItems(observableList);
        comboAlumnos.getSelectionModel().selectFirst();
    }
    
    @FXML
    public void cancelar(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    private boolean isEmptyInputs(){
        return comboAlumnos.getSelectionModel().getSelectedIndex()==0;
    }
    
    @FXML
    public void exito(ActionEvent event){
        if(isEmptyInputs()){
            this.showPopupMessage("Campos Vacios", "Favor de seleccionar un alumno");
            return;
        }
        EjerciciosPorAlumno ejerciciosPorAlumno = new EjerciciosPorAlumno();
        AlumnoConverter aux = (AlumnoConverter)comboAlumnos.getValue();
        Alumno alumno = aux.getAlumno();
        ejerciciosPorAlumno.setAlumnoId(alumno.getId());
        ejerciciosPorAlumno.setExito(true);
        ejerciciosPorAlumno.setEjercicioId(ejercicio.getId());
        this.port.addEjerciciosPorAlumno(ejerciciosPorAlumno);
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    @FXML
    public void error(ActionEvent event){
        if(isEmptyInputs()){
            this.showPopupMessage("Campos Vacios", "Favor de seleccionar un alumno");
            return;
        }
        EjerciciosPorAlumno ejerciciosPorAlumno = new EjerciciosPorAlumno();
        AlumnoConverter aux = (AlumnoConverter)comboAlumnos.getValue();
        Alumno alumno = aux.getAlumno();
        ejerciciosPorAlumno.setAlumnoId(alumno.getId());
        ejerciciosPorAlumno.setExito(false);
        ejerciciosPorAlumno.setEjercicioId(ejercicio.getId());
        this.port.addEjerciciosPorAlumno(ejerciciosPorAlumno);
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    public void setEjercicio(Ejercicio ejercicio){
        this.ejercicio = ejercicio;
    }
    
}
