/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Escuela;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import musicorum.EscuelaConverter;

/**
 * FXML Controller class
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class FXMLRegistryController extends BaseController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApMa;
    @FXML
    private TextField txtApPa;
    @FXML
    private TextField txtPwd;
    @FXML
    private TextField txtPwd1;
    @FXML
    private ChoiceBox comboEscuela;
    @FXML
    private TextField txtPwdEscuela;
    
    private Escuela escuela;
    private ObservableList observableList;
    List<Escuela> escuelas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        escuela = new Escuela();
        List<EscuelaConverter> escuelasCombo = new ArrayList<>();
        escuelas = port.getEscuelas();
        escuelas.forEach((x)->escuelasCombo.add(new EscuelaConverter(x)));
        observableList = FXCollections.observableArrayList();
        observableList.add("Seleccionar");
        observableList.add(new Separator());
        escuelasCombo.forEach((x) -> observableList.add(x));
        comboEscuela.setItems(observableList);
        comboEscuela.getSelectionModel().selectFirst();
    }

    private boolean isEmptyInputs() {
        if (txtPwd.getText().trim().isEmpty()) {
            return false;
        }
        if (txtPwd1.getText().trim().isEmpty()) {
            return false;
        }
        if (txtApMa.getText().trim().isEmpty()) {
            return false;
        }
        if (txtApPa.getText().trim().isEmpty()) {
            return false;
        }
        if (txtNombre.getText().trim().isEmpty()) {
            return false;
        }
        if (txtUsername.getText().trim().isEmpty()) {
            return false;
        }
        if(comboEscuela.getSelectionModel().getSelectedIndex()==0)return false;
        if(txtPwdEscuela.getText().trim().isEmpty())return false;
        return true;
    }

    @FXML
    public void registry(ActionEvent event) {
        Profesor profesor = new Profesor();
        
        if (!isEmptyInputs()) {
            this.showPopupMessage("Campos vacios","Favor de llenar todos los campos");
            return;
        }
        if (!txtPwd.getText().equals(txtPwd1.getText())) {
            this.showPopupMessage("Contraseña", "Las contraseñas no coinciden");
            return;
        }
        if(!port.checkUsername(txtUsername.getText())){
            this.showPopupMessage("Nombre de usuario", "Nombre de usuario ocupado \nFavor de seleccionar otro");
            return;
        }
        profesor.setUsername(txtUsername.getText().trim());
        profesor.setNombre(txtNombre.getText().trim());
        profesor.setApPaterno(txtApPa.getText().trim());
        profesor.setApMaterno(txtApMa.getText().trim());
        profesor.setPassword(Hashing.hash.sha1(txtPwd.getText().trim()));
        EscuelaConverter auxiliar = (EscuelaConverter)comboEscuela.getValue();
        Escuela selected = auxiliar.getEscuela();
        if(!escuelas.contains(selected)){
            selected.setPassword(Hashing.hash.sha1(selected.getPassword()));
            selected.setId(port.addEscuela(selected));
        }else{
            selected.setPassword(Hashing.hash.sha1(txtPwdEscuela.getText().trim()));
            if(!port.escuelaLogin(selected)){
                this.showPopupMessage("Login erroneo", "La contraseña de la institución es incorrecta\nIntente de nuevo");
                return;
            }
        }
        System.out.println(selected.getId());
        profesor.setIdEscuela(selected.getId());
        int result = port.register(profesor);
        if (result <= -1) {
            this.showPopupMessage("Error de aplicación", "Error al registrar");
            return;
        }
        profesor.setId(result);
        profesor.setPassword("");
        this.setParameter(profesor);
        this.gotoHome();
    }

    @FXML
    public void newEscuela() {
        try {
            FXMLNewEscuelaController escuelaController;
            FXMLLoader loader = new FXMLLoader(
                    getClass()
                            .getResource("/musicorum/views/FXMLNewEscuela.fxml")
            );
            Parent root = (Parent) loader.load();
            escuelaController = (FXMLNewEscuelaController) loader.getController();
            escuelaController.setEscuela(escuela);
            Stage popupstage = new Stage();
            popupstage.setTitle("Nueva Escuela");
            popupstage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(root));
            popupstage.showAndWait();
            if(!escuelaController.isState())return;
            observableList.add(new EscuelaConverter(escuela));
            comboEscuela.getSelectionModel().selectLast();
            txtPwdEscuela.setText(escuela.getPassword());
        } catch (IOException ex) {
            Logger.getLogger(FXMLRegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
