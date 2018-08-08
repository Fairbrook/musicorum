/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Alumno;
import WS.Ejercicio;
import WS.Grupo;
import WS.Unidad;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import musicorum.UnidadConverter;

/**
 * FXML Controller class
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class FXMLShowAlumnoController extends BaseController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lblNameProfesor;
    @FXML
    private Label lblNameAlumno;
    @FXML
    private Label lblAlumno;
    @FXML
    private Label lblSalon;
    @FXML
    private Label lblDesc;
    @FXML
    private Label lblCodigo;
    @FXML
    private TableView ejerciciosTable;
    @FXML
    private TableColumn columnNombre;
    @FXML
    private TextField filteredField;
    @FXML
    private ChoiceBox comboUnidades;
    private ObservableList observableList;
    private List<Unidad> unidades;
    private FilteredList<Ejercicio> filteredData;
    private Alumno alumno;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNameProfesor.setText(this.getParameter().getNombre());
         
    }    

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
        setData();
    }
    
    private void setData(){
        Grupo salon = this.port.getGrupoById(alumno.getGrupoId());
        String nombre = alumno.getNombre();
        nombre+=" "+alumno.getApPaterno();
        nombre+=" "+alumno.getApMaterno();
        unidades = this.port.getUnidadesByAlumno(alumno.getId());
        List<UnidadConverter> unidadesCombo = new ArrayList<>();
        unidades.forEach(x -> unidadesCombo.add(new UnidadConverter(x)));
        observableList = FXCollections.observableArrayList();
        observableList.add(0, "Todas");
        observableList.add(new Separator());
        observableList.addAll(unidadesCombo);
        comboUnidades.setItems(observableList);
        comboUnidades.getSelectionModel().selectFirst();
        lblNameAlumno.setText(alumno.getNombre());
        lblAlumno.setText(nombre);
        lblDesc.setText(alumno.getAnotaciones());
        lblSalon.setText(salon.getNombre());
        lblCodigo.setText(alumno.getCodigo());
        ObservableList observableList = FXCollections.observableArrayList();
        List<Ejercicio> ejercicios = this.port.getEjerciciosByAlumno(alumno.getId());
        ejercicios.forEach(x->observableList.add(x));
        filteredData = new FilteredList<>(observableList, p -> true);
        comboUnidades.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                filter();
            }
        });
        filteredField.textProperty().addListener((observable,oldValue,newValue)->{
            filter();
        });
        SortedList<Ejercicio> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ejerciciosTable.comparatorProperty());
        ejerciciosTable.setRowFactory(tv->{
            TableRow<Ejercicio> row = new TableRow<>();
            row.setOnMouseClicked(event->{
                if(event.getClickCount()==2&&(!row.isEmpty())){
                    Ejercicio rowData = row.getItem();
                    showEjercicio(rowData);
                }
            });
            return row;
        });
        columnNombre.setCellValueFactory(
                new PropertyValueFactory<Ejercicio,String>("nombre")
        );
        ejerciciosTable.setItems(sortedData);
    }
    
    private void filter() {
        filteredData.setPredicate(ejercicio -> {
            if (comboUnidades.getSelectionModel().getSelectedIndex() != 0) {
                UnidadConverter unidad = (UnidadConverter) comboUnidades.getValue();
                if (unidad.getUnidad().getId() == ejercicio.getUnidadId()) {
                    if (!filteredField.getText().trim().isEmpty()) {
                        String nombre = filteredField.getText().trim();
                        return ejercicio
                                .getNombre()
                                .toLowerCase()
                                .contains(nombre.toLowerCase());
                    } else {
                        return true;
                    }
                }
            } else if (!filteredField.getText().trim().isEmpty()) {
                String nombre = filteredField.getText().trim();
                return ejercicio
                        .getNombre()
                        .toLowerCase()
                        .contains(nombre.toLowerCase());
            } else {
                return true;
            }
            return false;
        });
    }
    
    private void showEjercicio(Ejercicio ejercicio){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass()
                    .getResource("/musicorum/views/FXMLShowEstadistics.fxml")
            );
            Parent root = (Parent) loader.load();
            FXMLShowEstadisticsController controller = (FXMLShowEstadisticsController) loader.getController();
            controller.set(ejercicio, alumno);
            Stage popupstage = new Stage();
            popupstage.setTitle("Ejercicios resuletos");
            popupstage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(root));
            popupstage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLShowAlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void updateAlumno(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass()
                    .getResource("/musicorum/views/FXMLUpdateAlumno.fxml")
            );
            Parent root = (Parent) loader.load();
            FXMLUpdateAlumnoController controller = (FXMLUpdateAlumnoController) loader.getController();
            controller.setProfesor(this.getParameter());
            controller.setAlumno(alumno);
            Stage popupstage = new Stage();
            popupstage.setTitle("Actualizar alumno");
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(root));
            popupstage.showAndWait();
            setData();
        } catch (IOException ex) {
            Logger.getLogger(FXMLShowAlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
