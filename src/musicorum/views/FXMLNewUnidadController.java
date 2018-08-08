/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Unidad;
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
public class FXMLNewUnidadController extends BaseController implements Initializable {

    @FXML
    private TextField txtNombre;
    private Unidad unidad;
    private boolean state;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        state=false;
    }    
    
    @FXML
    public void aceptar(ActionEvent event){
        if(!isEmptyInputs()){
            this.showPopupMessage("Campos vacios", "Favor de llenar todos los campos");
            return;
        }
        unidad.setNombre(txtNombre.getText().trim());
        state = true;
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    @FXML
    public void cancelar(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    private boolean isEmptyInputs(){
       if(txtNombre.getText().trim().isEmpty()) return false;
       return true;
    }
    
    public void setUnidad(Unidad unidad){
        this.unidad = unidad;
    }
    
    public boolean isState(){
        return state;
    }
    
}
