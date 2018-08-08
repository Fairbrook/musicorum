/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Profesor;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 *
 * @author kevin
 */
public class FXMLLoginController extends BaseController implements Initializable {
    
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPwd;
    
    private boolean isEmptyInputs(){
        if(txtUsername.getText().trim().isEmpty())return false;
        if(txtPwd.getText().trim().isEmpty())return false;
        return true;
    }
    
    @FXML
    private void login(ActionEvent event) {
        if(!isEmptyInputs()){ 
            this.showPopupMessage("Campos vacíos", "Favor de introducir usuario y contraseña");
            return;
        }
        Profesor profesor = new Profesor();
        profesor.setUsername(txtUsername.getText().trim());
        profesor.setPassword(Hashing.hash.sha1(txtPwd.getText().trim()));
        int id=this.port.login(profesor);
        if(id==-1){
            this.showPopupMessage("Login incorrecto", "Usuario o contraseña no válidos\nIntentelo de nuevo");
            return;
        }
        profesor = port.getProfesor(id);
        //System.out.println(port.login(profesor));
        this.setParameter(profesor);
        this.gotoHome();
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
