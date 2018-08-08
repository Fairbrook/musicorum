/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum;

import WS.Grupo;

/**
 *
 * @author kevin
 */
public class GrupoConverter{
    private final Grupo grupo;
    
    public GrupoConverter(Grupo grupo){
        this.grupo=grupo;
    }

    public Grupo getGrupo() {
        return grupo;
    }
    
    @Override
    public String toString() {
        return this.grupo.getNombre();
    }
}
