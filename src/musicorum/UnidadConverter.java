/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum;

import WS.Unidad;

/**
 *
 * @author kevin
 */
public class UnidadConverter {
    private final Unidad unidad;
    
    public UnidadConverter(Unidad unidad){
        this.unidad = unidad;
    }
    
    public Unidad getUnidad(){
        return this.unidad;
    }
    
    @Override
    public String toString(){
        return this.unidad.getNombre();
    }
}
