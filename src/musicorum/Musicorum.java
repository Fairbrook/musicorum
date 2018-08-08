/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import musicorum.views.BaseController;

/**
 *
 * @author kevin
 */
public class Musicorum extends Application {
    private Stage stage;
    
    @Override
    public void start(Stage stage){
        stage.setResizable(false);
        this.stage = stage;
        this.stage.setTitle("Musicorum");
        changeScene("/musicorum/views/FXMLLogin.fxml");
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("views/img/icon.png")));
    }

    public BaseController changeScene(String scene){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(scene)
            );
            Parent root = (Parent)loader.load();
            BaseController controller = (BaseController) loader.getController();
            controller.setLogic(this);
            stage.setScene(new Scene(root));
            stage.show();
            return controller;
        } catch (IOException ex) {
            return null;
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
