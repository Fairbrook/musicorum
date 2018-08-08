/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musicorum;

import WS.Alumno;

/**
 *
 * @author kevin
 */
public class AlumnoConverter {
    public final Alumno alumno;
    
    public AlumnoConverter(Alumno alumno){
        this.alumno = alumno;
    }
    
    public Alumno getAlumno(){
        return this.alumno;
    }
    
    @Override
    public String toString(){
        return this.alumno.getNombre();
    }
}
