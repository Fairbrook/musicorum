/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum.views;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import midi.MIDIPlayer;
import org.jfugue.midi.MidiFileManager;
import org.jfugue.pattern.Pattern;

/**
 *
 * @author kevin
 */
public class EjercicioController extends BaseController{
    
    @FXML
    protected GridPane gridPane;
    protected final String[] images = {"whole.png", "half.png", "quarter.png", "eight.png"};
    protected final String[] sounds = {"E3","F3","G3", "A3", "B3","C4", "D4", "E4", "F4", "G4", "A4","C", "D", "E", "F", "G", "A", "B", "C6", "D6", "E6", "F6"};
    protected String[][] sets;
    protected Pattern pattern;
    protected int tempo;
    protected boolean clave;
    
    protected void makePattern(){
        int pos;
        String patron = "";
        for (int l = 0; l < 20; l++) {
            if (sets[l][0] != null) {
                pos = 11 - Integer.parseInt(sets[l][1]);
                if(clave==true)pos+=11;
                patron += sounds[pos] + mapForMidi(sets[l][0]) + " ";
            }
        }
        pattern = new Pattern(patron).setTempo(tempo).setInstrument("Piano");
    }
    
    @FXML
    public void play() {
        try {
            makePattern();
            File file = new File("out.midi");
            MidiFileManager.savePatternToMidi(pattern, file);
            Thread hilo = new Thread(new MIDIPlayer(file));
            hilo.start();
        } catch (IOException ex) {
            
        }
    }

    protected String mapForMidi(String string) {
        if (string.equals("half.png"))return "h";
        if (string.equals("quarter.png")) return "q";
        if (string.equals("whole.png")) return "w";
        if (string.equals("eight.png")) return "i";
        return "";
    }
}
