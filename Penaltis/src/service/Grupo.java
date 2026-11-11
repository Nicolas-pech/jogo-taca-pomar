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
public class Grupo {

    private Long id;
    private String nome;
    private List<Time> times;

    public Grupo() {
        this.times = new ArrayList<>();
    }

    public Grupo(Long id, String nome) {
        this.id = id;
        this.nome = nome;
        this.times = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }
    
}
