/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.io.Serializable;
import java.sql.Date;


/**
 *
 * @author Iván
 */
public class Ave implements Serializable{
    String anilla;
    String especie;
    String lugar;
    Date fecha;

    public Ave(String anilla, String especie, String lugar, Date fecha) {
        this.anilla = anilla;
        this.especie = especie;
        this.lugar = lugar;
        this.fecha = fecha;
    }

    public Ave() {
        
    }

    @Override
    public String toString() {
        return "Aves{" + "anilla=" + anilla + ", especie=" + especie + ", lugar=" + lugar + ", fecha=" + fecha + '}';
    }
    
    public String getAnilla() {
        return anilla;
    }

    public void setAnilla(String anillas) {
        this.anilla = anillas;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
}
