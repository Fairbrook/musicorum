 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Alumno;
import WS.Ejercicio;
import WS.Musicorum_Service;
import WS.Profesor;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import musicorum.Musicorum;

/**
 *
 * @author kevin
 */
public class BaseController {
    protected Musicorum logic;
    protected Musicorum_Service service;
    protected WS.Musicorum port;
    private static Profesor profesor;
    
    BaseController(){
        logic = null;
        service = new Musicorum_Service();
        port = service.getMusicorumPort();
    }
    
    BaseController(Musicorum logic){
        this.logic = logic;
    }
    
    public void setLogic(Musicorum logic){
        this.logic = logic;
    }
    
    public Musicorum getLogic(){
        return this.logic;
    }

    public Profesor getParameter() {
        return profesor;
    }

    public void setParameter(Profesor parameter) {
        this.profesor = parameter;
    }
    
    @FXML
    public void gotoHome(){
        BaseController controller;
        controller = this.logic.changeScene("/musicorum/views/FXMLHome.fxml");
        //controller.profesor = this.profesor;
    }
    
    @FXML
    public void gotoAddAlumno(){
        this.logic.changeScene("/musicorum/views/FXMLAddAlumno.fxml");
    }
    
    @FXML
    public void gotoShowEjercicios(){
        this.logic.changeScene("/musicorum/views/FXMLShowEjercicios.fxml");
    }
    
    @FXML
    public void gotoViewAlumno(Profesor prof){
        this.logic.changeScene("/musicorum/views/ViewAlumno.fxml");
    }
    
    @FXML
    public void gotoLogin(){
        this.logic.changeScene("/musicorum/views/FXMLLogin.fxml");
    }
    
    @FXML
    public void gotoAddEjercicio(){
        this.logic.changeScene("/musicorum/views/FXMLAddEjercicio.fxml");
    }
    
    @FXML
    public void gotoRegistry(){
        this.logic.changeScene("/musicorum/views/FXMLRegistry.fxml");
    }
    
    @FXML
    public void gotoShowEjercicio(Ejercicio ejercicio){
        FXMLShowEjercicioController ejercicioController;
        ejercicioController = (FXMLShowEjercicioController)this.logic.changeScene("/musicorum/views/FXMLShowEjercicio.fxml");
        ejercicioController.setEjercicio(ejercicio);
    }
    
    @FXML
    public void gotoShowAlumno(Alumno alumno){
        FXMLShowAlumnoController controller;
        controller = (FXMLShowAlumnoController)this.logic.changeScene("/musicorum/views/FXMLShowAlumno.fxml");
        controller.setAlumno(alumno);
    }
    
    public void showPopupMessage(String titulo, String mensaje){
        
        try {
            FXMLMessagesController mensajeController;
            FXMLLoader loader = new FXMLLoader(
                    getClass()
                            .getResource("/musicorum/views/FXMLMessages.fxml")
            );
            Parent root = (Parent)loader.load();
            mensajeController = (FXMLMessagesController)loader.getController();
            mensajeController.setMensaje(mensaje);
            mensajeController.setTitulo(titulo);
            Stage popupstage = new Stage();
            popupstage.setTitle("Informacion");
            popupstage.getIcons().add(new Image(getClass().getResourceAsStream("img/if_police_309038.png")));
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(root));
            popupstage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(BaseController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
