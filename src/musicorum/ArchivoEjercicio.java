/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum;

import java.io.Serializable;

/**
 *
 * @author kevin
 */
public class ArchivoEjercicio implements Serializable{
    private boolean clave;
    private String[][] sets;

    public ArchivoEjercicio() {
        clave = true;
        sets = new String[20][2];
    }

    public boolean isClave() {
        return clave;
    }

    public void setClave(boolean clave) {
        this.clave = clave;
    }

    public String[][] getSets() {
        return sets;
    }

    public void setSets(String[][] sets) {
        this.sets = sets;
    }
    
    
}
