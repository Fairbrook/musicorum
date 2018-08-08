/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum;

import WS.Escuela;

/**
 *
 * @author kevin
 */
public class EscuelaConverter {
    public final Escuela escuela;
    
    public EscuelaConverter(Escuela escuela){
        this.escuela = escuela;
    }
    
    public Escuela getEscuela(){
        return this.escuela;
    }
    
    @Override
    public String toString(){
        return this.escuela.getNombre();
    }
}
