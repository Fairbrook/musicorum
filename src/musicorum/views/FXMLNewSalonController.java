/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Grupo;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author kevin
 */
public class FXMLNewSalonController implements Initializable {

    private Grupo grupo;
    @FXML
    private TextField txtNombre;
    private boolean state;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        state=false;
    }    
    
    @FXML
    public void aceptar(ActionEvent event){
        grupo.setNombre(txtNombre.getText());
        state=true;
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    @FXML
    public void cancelar(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public boolean isState() {
        return state;
    }
    
    
}
