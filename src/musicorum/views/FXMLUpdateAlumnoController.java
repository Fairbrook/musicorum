/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Alumno;
import WS.Grupo;
import WS.Profesor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import musicorum.GrupoConverter;

/**
 * FXML Controller class
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class FXMLUpdateAlumnoController extends BaseController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    @FXML
    private ChoiceBox comboSalon;
    @FXML
    private Label lblNameProfesor;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApPa;
    @FXML
    private TextField txtApMa;
    @FXML
    private TextArea txtAnotaciones;
    private Alumno alumno;
    private Grupo grupo;
    private Profesor profesor;
    private ObservableList observableList;
    private List<Grupo> grupos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNameProfesor.setText(this.getParameter().getNombre());
        grupos = port.getGruposByProfesor(this.getParameter().getId());
        List<GrupoConverter> gruposCombo = new ArrayList<>();
        grupos.forEach((x)->gruposCombo.add(new GrupoConverter(x)));
        observableList = FXCollections.observableArrayList();
        observableList.add("Selecionar");
        observableList.add(new Separator());
        gruposCombo.forEach((x)->observableList.add(x));
        comboSalon.setItems(observableList);
        comboSalon.getSelectionModel().selectFirst();
    }    

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
        setData();        
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
    
    private void setData(){
        comboSalon.setItems(observableList);
        txtNombre.setText(alumno.getNombre());
        txtApPa.setText(alumno.getApPaterno());
        txtApMa.setText(alumno.getApMaterno());
        txtAnotaciones.setText(alumno.getAnotaciones());
        comboSalon.getSelectionModel().select(alumno.getGrupoId());
    }
    
    private boolean isEmptyInputs(){
        if(txtApPa.getText().trim().isEmpty())return false;
        if(txtNombre.getText().trim().isEmpty())return false;
        if(txtApMa.getText().trim().isEmpty())return false;
        if(comboSalon.getSelectionModel().getSelectedIndex()==0)return false;
        return true;
    }
    
    @FXML
    public void updateAlumno(ActionEvent event){
        Grupo nuevo;
        boolean isNuevo = false;
        if (!isEmptyInputs()) {
            this.showPopupMessage("Campos vacios","Favor de llenar todos los campos");
            return;
        }        
        alumno.setAnotaciones(txtAnotaciones.getText().trim());
        alumno.setApMaterno(txtApMa.getText().trim());
        alumno.setApPaterno(txtApPa.getText().trim());
        GrupoConverter auxiliar= (GrupoConverter)comboSalon.getValue();
        nuevo = auxiliar.getGrupo();
        if(!grupos.contains(nuevo)){
            isNuevo = true;
            nuevo.setId(port.addGrupo(nuevo));
            port.addGruposPorProfesor(nuevo, this.getParameter());
        }
        alumno.setGrupoId(nuevo.getId());
        alumno.setNombre(txtNombre.getText().trim());
        int id = port.updateAlumno(alumno);
        if(id==-1){
            if(isNuevo)port.deleteGrupo(nuevo);
            this.showPopupMessage("Error de aplicacion", "Error al Actualizar alumno");
            return;
        }
        this.showPopupMessage("Agregado con éxito", "El alumno ha sido actualizado con éxito");
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    @FXML
    public void newGrupo() {
        try {
            grupo = new Grupo();
            grupo.setIdEscuela(this.getParameter().getIdEscuela());
            FXMLNewSalonController grupoController;
            FXMLLoader loader = new FXMLLoader(
                    getClass()
                            .getResource("/musicorum/views/FXMLNewSalon.fxml")
            );
            Parent root = (Parent) loader.load();
            grupoController = (FXMLNewSalonController) loader.getController();
            grupoController.setGrupo(grupo);
            Stage popupstage = new Stage();
            popupstage.setTitle("Nuevo Grupo");
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(root));
            popupstage.showAndWait();
            if(!grupoController.isState())return;
            observableList.add(new GrupoConverter(grupo));
            comboSalon.getSelectionModel().selectLast();
        } catch (IOException ex) {
            Logger.getLogger(FXMLRegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
