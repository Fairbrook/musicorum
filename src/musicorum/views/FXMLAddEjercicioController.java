/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Ejercicio;
import WS.Unidad;
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import musicorum.ArchivoEjercicio;
import musicorum.UnidadConverter;
import org.jfugue.midi.MidiFileManager;

public class FXMLAddEjercicioController extends EjercicioController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lblNameProfesor;
    @FXML
    private HBox notes;
    @FXML
    private TextField txtNombre;
    @FXML
    private ChoiceBox comboUnidad;
    @FXML
    private ImageView key;
    @FXML
    private ImageView signature;
    private Unidad unidad;
    private Map<Image, String> imagesToCompare;
    private ObservableList observableList;
    List<Unidad> unidades;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lblNameProfesor.setText(this.getParameter().getNombre());
        imagesToCompare = new HashMap<>();
        sets = new String[20][2];
        setInitialImages();
        setTargets();
        tempo = 60;
        unidades = this.port.getUnidades(this.getParameter().getId());
        List<UnidadConverter> unidadesCombo = new ArrayList<>();
        unidades.forEach(x->unidadesCombo.add(new UnidadConverter(x)));
        observableList = FXCollections.observableArrayList();
        observableList.add(0,"Seleccionar");
        observableList.add(new Separator());
        observableList.addAll(unidadesCombo);
        comboUnidad.setItems(observableList);
        comboUnidad.getSelectionModel().selectFirst();
        Image imagen = new Image(getClass().getResource("img/notes/fa.png").toString());
        key.setImage(imagen);
        signature.setFitHeight(258);
        this.clave = true;
    }
    
    
    @FXML
    public void save(){
        try {
            makePattern();
            Ejercicio ejercicio = new Ejercicio();
            Gson gson = new Gson();
            if (!isEmptyInputs()) {
                this.showPopupMessage("Campos vacios", "Favor de llenar todos los campos");
                return;
            }
            UnidadConverter unidadConverter = (UnidadConverter)comboUnidad.getValue();
            Unidad unidad = unidadConverter.getUnidad();
            if(!unidades.contains(unidad))unidad.setId(this.port.addUnidad(unidad));
            ejercicio.setProfesorId(getParameter().getId());
            ejercicio.setNombre(txtNombre.getText().trim());
            ejercicio.setUnidadId(unidad.getId());
            File file = new File("out.midi");
            MidiFileManager.savePatternToMidi(pattern, file);
            Path path = Paths.get("out.midi");
            ArchivoEjercicio archivoEjercicio = new ArchivoEjercicio();
            archivoEjercicio.setClave(clave);
            archivoEjercicio.setSets(sets);
            this.port.addEjercicio(ejercicio, Files.readAllBytes(path), gson.toJson(archivoEjercicio));
            this.showPopupMessage("Ejericcio guardado", "Ejericcio guardado con éxito");
            this.gotoShowEjercicios();
        } catch (IOException ex) {
            this.showPopupMessage("Error", "Archivo midi no creado");
        }
    }
    
    @FXML
    public void newUnidad() {
        try {
            unidad = new Unidad();
            FXMLNewUnidadController unidadController;
            FXMLLoader loader = new FXMLLoader(
                    getClass()
                            .getResource("/musicorum/views/FXMLNewUnidad.fxml")
            );
            Parent root = (Parent) loader.load();
            unidadController = (FXMLNewUnidadController) loader.getController();
            unidadController.setUnidad(unidad);
            Stage popupstage = new Stage();
            popupstage.setTitle("Nueva Unidad");
            popupstage.getIcons().add(new Image(getClass().getResourceAsStream("img/icon.png")));
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(root));
            popupstage.showAndWait();
            if(!unidadController.isState())return;
            observableList.add(new UnidadConverter(unidad));
            comboUnidad.getSelectionModel().selectLast();
        } catch (IOException ex) {
            Logger.getLogger(FXMLRegistryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void changeKey(){
        Image imagen;
        Image imageKey;
        if(clave==true){
            imageKey = new Image(getClass().getResource("img/notes/fa.png").toString());
            imagen= new Image(getClass().getResource("img/notes/key.png").toString());
        }else{
            imageKey = new Image(getClass().getResource("img/notes/key.png").toString());
            imagen= new Image(getClass().getResource("img/notes/fa.png").toString());
        }
        clave = !clave;
        key.setImage(imagen);
        signature.setImage(imageKey);
    }
    
    private boolean isEmptyInputs(){
        if(txtNombre.getText().trim().isEmpty())return false;
        if(comboUnidad.getSelectionModel().getSelectedIndex()==0)return false;
        for(int x = 0; x<20; x++){
            if(sets[x][0]!=null)return true;
        }
        return false;
    }
    
    private void setTargets() {
        ImageView imageView;
        Image image = new Image(getClass().getResource("img/notes/circle.png").toString());
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 20; y++) {
                imageView = new ImageView(image);
                imageView.setFitHeight(20);
                imageView.setFitWidth(30);
                gridPane.add(imageView, y, x);
                setDropFeature(imageView);
            }
        }
    }

    private void setInitialImages() {
        Image image;
        ImageView imageView;
        for (String x : images) {
            image = new Image(getClass().getResource("img/notes/" + x).toString());
            imagesToCompare.put(image, x);
            imageView = new ImageView(image);
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            setDragFeature(imageView);
            notes.getChildren().add(imageView);
        }
    }

    private void setDragFeature(ImageView imageView) {
        imageView.setOnDragDetected(event -> {
            Dragboard dragboard = imageView.startDragAndDrop(TransferMode.COPY);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putImage(imageView.getImage());
            clipboardContent.putString(imagesToCompare.get(imageView.getImage()));
            dragboard.setContent(clipboardContent);
            event.consume();
        });

        imageView.setOnDragDone(event -> {
            event.consume();
        });
    }

    private void setDropFeature(ImageView imageView) {
        imageView.setOnDragOver(event -> {
            Integer column = GridPane.getColumnIndex(imageView);
            if (event.getDragboard().hasImage() && sets[column][0]==null) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
        });
        
        imageView.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasImage()) {
                imageView.setImage(dragboard.getImage());
                Integer column = GridPane.getColumnIndex(imageView);
                Integer row = GridPane.getRowIndex(imageView);
                sets[column][0] = dragboard.getString();
                sets[column][1] = row.toString();
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
        
        imageView.setOnDragDetected(event->{
            Dragboard dragboard;
            ClipboardContent clipboardContent = new ClipboardContent();
            Integer column = GridPane.getColumnIndex(imageView);
            if(sets[column][0]!=null){
                dragboard = imageView.startDragAndDrop(TransferMode.MOVE);
                clipboardContent.putImage(imageView.getImage());
                clipboardContent.putString(sets[column][0]);
                dragboard.setContent(clipboardContent);
                sets[column][0]=null;
            }
            event.consume();
        });
        
        imageView.setOnDragDone(event -> {
            Integer column = GridPane.getColumnIndex(imageView);
            if (sets[column][0] == null) {
                Image image = new Image(getClass().getResource("img/notes/circle.png").toString());
                imageView.setImage(image);
            }
            event.consume();
        });
    }

}
