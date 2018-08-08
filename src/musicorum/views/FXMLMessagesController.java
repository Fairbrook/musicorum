/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author kevin
 */
public class FXMLMessagesController implements Initializable {

    @FXML
    private Label txtMensaje;
    @FXML
    private Label txtTitle;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        txtTitle.setText("Error de aplicacion");
        txtMensaje.setText("Error");
    }    
    
    @FXML
    public void aceptar(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }

    public void setMensaje(String mensaje) {
        txtMensaje.setText(mensaje);
        
    }

    public void setTitulo(String titulo) {
        txtTitle.setText(titulo);
    }
    
    
}
