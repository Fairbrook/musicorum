/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import WS.Grupo;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import musicorum.GrupoConverter;

/**
 * FXML Controller class
 *
 * @author kevin
 */
public class FXMLNewGrupoPorProfesorController extends BaseController implements Initializable {

    @FXML
    private ChoiceBox comboSalon;
    private ObservableList observableList;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Grupo> all = this.port.getGruposByEscuela(this.getParameter().getIdEscuela());
        List<Grupo> profesor = this.port.getGruposByProfesor(this.getParameter().getId());
        List<Grupo> remove = new ArrayList<>();
        all.forEach(x->{
            profesor.forEach(y->{
                if(x.getId()==y.getId())remove.add(x);
            });
        });
        all.removeAll(remove);
        
        List<GrupoConverter> gruposCombo = new ArrayList<>();
        all.forEach((x)->gruposCombo.add(new GrupoConverter(x)));
        observableList = FXCollections.observableArrayList();
        observableList.add("Selecionar");
        observableList.add(new Separator());
        gruposCombo.forEach((x)->observableList.add(x));
        comboSalon.setItems(observableList);
        comboSalon.getSelectionModel().selectFirst();
    }    
    
    private boolean isEmptyInputs(){
        return comboSalon.getSelectionModel().getSelectedIndex()==0;
    }
    
    @FXML
    public void aceptar(ActionEvent event){
        if(isEmptyInputs()){
            this.showPopupMessage("Campos vacios", "Seleccione un salon");
            return;
        }
        GrupoConverter aux = (GrupoConverter)comboSalon.getValue();
        this.port.addGruposPorProfesor(aux.getGrupo(), this.getParameter());
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
    
    @FXML
    public void cancelar(ActionEvent event){
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
}
