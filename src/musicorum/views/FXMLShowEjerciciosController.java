/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Ejercicio;
import WS.Unidad;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import musicorum.UnidadConverter;

/**
 * FXML Controller class
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class FXMLShowEjerciciosController extends BaseController implements Initializable {

    @FXML
    private Label lblNameProfesor;
    @FXML
    private TableView<Ejercicio> ejerciciosTable;
    @FXML
    private TableColumn<Ejercicio, String> columnNombre;
    @FXML
    private TextField filteredField;
    @FXML
    private ChoiceBox comboUnidades;
    private ObservableList observableList;
    private List<Unidad> unidades;
    private FilteredList<Ejercicio> filteredData;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNameProfesor.setText(this.getParameter().getNombre());
        unidades = this.port.getUnidades(this.getParameter().getId());
        List<UnidadConverter> unidadesCombo = new ArrayList<>();
        unidades.forEach(x -> unidadesCombo.add(new UnidadConverter(x)));
        observableList = FXCollections.observableArrayList();
        observableList.add(0, "Todas");
        observableList.add(new Separator());
        observableList.addAll(unidadesCombo);
        comboUnidades.setItems(observableList);
        comboUnidades.getSelectionModel().selectFirst();
        configureTable();
    }

    private void configureTable() {
        ObservableList<Ejercicio> observableList = FXCollections.observableArrayList();
        List<Ejercicio> ejercicios = this.port.getEjercicios(this.getParameter().getId());
        ejercicios.forEach(x -> observableList.add(x));
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
        ejerciciosTable.setRowFactory(tv -> {
            TableRow<Ejercicio> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Ejercicio rowData = row.getItem();
                    showEjercicio(rowData);
                }
            });
            return row;
        });

        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        ejerciciosTable.setItems(sortedData);
    }

    private void showEjercicio(Ejercicio ejercicio) {
        this.gotoShowEjercicio(ejercicio);
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
}
