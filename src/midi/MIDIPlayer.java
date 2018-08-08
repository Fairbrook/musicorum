/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package midi;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

/**
 *
 * @author kevin
 */
public class MIDIPlayer extends Thread{
    
    private final File midiFile;
    
    public MIDIPlayer(File midi){
        midiFile = midi;
    }
    
    @Override
    public void run(){
        if(!midiFile.exists() || midiFile.isDirectory() || !midiFile.canRead()){
            return;
        }
        play();
    }
    
    public void play(){
         try {
            Sequencer sequencer = MidiSystem.getSequencer();
            sequencer.setSequence(MidiSystem.getSequence(midiFile));
            sequencer.open();
            sequencer.start();
            while(sequencer.isRunning()) {
                try {
                    Thread.sleep(500);
                } catch(InterruptedException ignore) {
                    break;
                }
            }
            sequencer.stop();
            sequencer.close();
        } catch(MidiUnavailableException mue) {
            System.out.println("Midi device unavailable!");
        } catch(InvalidMidiDataException imde) {
            System.out.println("Invalid Midi data!");
        } catch(IOException ioe) {
            System.out.println("I/O Error!");
        }
    }
}
