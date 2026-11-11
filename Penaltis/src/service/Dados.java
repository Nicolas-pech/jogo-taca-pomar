/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mauri
 */
public class Dados {
    
    private List<Temporada> temporadas;

    public Dados() {
    }

    public Dados(List<Temporada> temporadas) {
        this.temporadas = new ArrayList<>();
    }

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
    }
    
}
