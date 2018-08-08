/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Alumno;
import WS.Grupo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import musicorum.GrupoConverter;

/**
 * FXML Controller class
 *
 * @author kevin
 */
public class FXMLHomeController extends BaseController implements Initializable {

    @FXML
    private Label lblNameProfesor;
    @FXML
    private TableView alumnosTable;
    @FXML
    private TableColumn columnNombre;
    @FXML
    private TableColumn columnClave;
    @FXML
    private TextField filteredField;
    @FXML
    private ChoiceBox comboSalones;
    private ObservableList observableList;
    private List<Grupo> salones;
    private FilteredList<Alumno> filteredData;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNameProfesor.setText(this.getParameter().getNombre());
        salones = this.port.getGruposByProfesor(this.getParameter().getId());
        List<GrupoConverter> grupos = new ArrayList<>();
        salones.forEach(x->grupos.add(new GrupoConverter(x)));
        observableList = FXCollections.observableArrayList();
        observableList.add(0, "Todos");
        observableList.add(new Separator());
        observableList.addAll(grupos);
        comboSalones.setItems(observableList);
        comboSalones.getSelectionModel().selectFirst();
        configureTable();
    }
    
    private void configureTable(){
        ObservableList observableList = FXCollections.observableArrayList();
        List<Alumno> alumnos = this.port.getAlumnos(this.getParameter().getId());
        alumnos.forEach((alumno)->observableList.add(alumno));
        filteredData = new FilteredList<>(observableList,p->true);
        comboSalones.valueProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
            filter();
        });
        filteredField.textProperty().addListener((observable, oldValue, newValue)->{
            filter();
        });
        SortedList<Alumno>sorteredData = new SortedList<>(filteredData);
        sorteredData.comparatorProperty().bind(alumnosTable.comparatorProperty());
        alumnosTable.setRowFactory(tv->{
            TableRow<Alumno> row = new TableRow<>();
            row.setOnMouseClicked(event->{
                if(event.getClickCount()==2&&(!row.isEmpty())){
                    Alumno rowData = row.getItem();
                    showAlumno(rowData);
                }
            });
            return row;
        });
        columnNombre.setCellValueFactory(
                new PropertyValueFactory<Alumno,String>("nombre")
        );
        columnClave.setCellValueFactory(
                new PropertyValueFactory<Alumno,String>("codigo")
        );
        alumnosTable.setItems(sorteredData);
    }
    
    private void filter(){
        filteredData.setPredicate(alumno -> {
            if (comboSalones.getSelectionModel().getSelectedIndex() != 0) {
                GrupoConverter grupo = (GrupoConverter) comboSalones.getValue();
                if (grupo.getGrupo().getId() == alumno.getGrupoId()) {
                    if (!filteredField.getText().trim().isEmpty()) {
                        String nombre = filteredField.getText().trim();
                        return alumno
                                .getNombre()
                                .toLowerCase()
                                .contains(nombre.toLowerCase());
                    } else {
                        return true;
                    }
                }
            } else if (!filteredField.getText().trim().isEmpty()) {
                String nombre = filteredField.getText().trim();
                return alumno
                        .getNombre()
                        .toLowerCase()
                        .contains(nombre.toLowerCase());
            } else {
                return true;
            }
            return false;
        });
    }
    
    private void showAlumno(Alumno alumno){
        this.gotoShowAlumno(alumno);
    }
    
    @FXML
    public void vincularGrupo() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass()
                            .getResource("/musicorum/views/FXMLNewGrupoPorProfesor.fxml")
            );
            Parent root = (Parent) loader.load();
            Stage popupstage = new Stage();
            popupstage.setTitle("Vincluar salon");
            popupstage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(root));
            popupstage.showAndWait();
            configureTable();
        } catch (IOException ex) {
            Logger.getLogger(FXMLRegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
