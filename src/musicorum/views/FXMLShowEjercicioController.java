/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Ejercicio;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import musicorum.ArchivoEjercicio;

/**
 * FXML Controller class
 *
 * @author kevin
 */
public class FXMLShowEjercicioController extends EjercicioController implements Initializable {

    @FXML
    private Label lblNameProfesor;
    @FXML
    private Label lblNameEjercicio;
    @FXML
    private Label lblUnidad;
    @FXML
    private Ejercicio ejercicio;
    @FXML
    private ImageView key;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNameProfesor.setText(this.getParameter().getNombre());
        key.setFitHeight(258);
        tempo = 60;
    }    
    
    public void setEjercicio(Ejercicio ejercicio){
        this.ejercicio = ejercicio;
        setData();
    }
    
    private void setData(){
        Gson gson = new Gson();
        lblNameEjercicio.setText(this.ejercicio.getNombre());
        lblUnidad.setText(this.port.getUnidadById(this.ejercicio.getUnidadId()).getNombre());
        String info = this.port.getEjercicioFile(ejercicio.getArchivo());
        ArchivoEjercicio archivoEjercicio = gson.fromJson(info, ArchivoEjercicio.class);
        this.sets = archivoEjercicio.getSets();
        this.clave = archivoEjercicio.isClave();
        if(!this.clave)key.setImage(new Image(getClass().getResource("img/notes/fa.png").toString()));
        ImageView imageView;
        Image image=null;
        String nombreArchivo;
        for (Integer x = 0; x < 11; x++) {
            for (Integer y = 0; y < 20; y++) {
                nombreArchivo = "circle.png";
                if(sets[y][0]!=null){
                    if(sets[y][1].equals(x.toString())){
                        nombreArchivo = sets[y][0];
                    }
                }
                image = new Image(getClass().getResource("img/notes/"+nombreArchivo).toString());
                imageView = new ImageView(image);
                imageView.setFitHeight(20);
                imageView.setFitWidth(30);
                gridPane.add(imageView, y, x);
            }
        }
    }
    
    @FXML
    private void doEjercicio(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass()
                    .getResource("/musicorum/views/FXMLDoEjercicio.fxml")
            );
            Parent root = (Parent) loader.load();
            FXMLDoEjercicioController controller = (FXMLDoEjercicioController) loader.getController();
            controller.setEjercicio(ejercicio);
            Stage popupstage = new Stage();
            popupstage.setTitle("Aplicar ejercicio");
            popupstage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(root));
            popupstage.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLShowAlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
