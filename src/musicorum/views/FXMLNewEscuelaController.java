/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Escuela;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author kevin
 */
public class FXMLNewEscuelaController extends BaseController implements Initializable {

    private Escuela escuela;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPwd;
    @FXML
    private TextField txtPwd1;
    private boolean state;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        state = false;
    }

    private boolean isEmptyInputs(){
        if(txtNombre.getText().trim().isEmpty())return false;
        if(txtPwd.getText().trim().isEmpty())return false;
        if(txtPwd1.getText().trim().isEmpty())return false;
        return true;
    }
    
    @FXML
    public void aceptar(ActionEvent event){
        if(!isEmptyInputs()){
            this.showPopupMessage("Campos vacios", "Favor de llenar todos los campos");
            return;
        }
        if(!txtPwd.getText().trim().equals(txtPwd1.getText().trim())){
            this.showPopupMessage("Contraseña", "Las contraseñas no coiciden");
            return;
        }
        escuela.setNombre(txtNombre.getText().trim());
        escuela.setPassword(txtPwd.getText().trim());
        state = true;
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    @FXML
    public void cancelar(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    public Escuela getEscuela() {
        return escuela;
    }

    public void setEscuela(Escuela escuela) {
        this.escuela = escuela;
    }

    public boolean isState() {
        return state;
    }
    
    
    
}
