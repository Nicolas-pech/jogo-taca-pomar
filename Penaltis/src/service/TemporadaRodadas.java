package service;

import java.util.ArrayList;
import java.util.List;

public class TemporadaRodadas {

    private Long temporadaId;
    private int ano;
    private List<PartidaRodada> partidas;
    private List<String> confrontosGerados;

    public TemporadaRodadas() {
        this.partidas = new ArrayList<>();
        this.confrontosGerados = new ArrayList<>();
    }

    public Long getTemporadaId() {
        return temporadaId;
    }

    public void setTemporadaId(Long temporadaId) {
        this.temporadaId = temporadaId;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public List<PartidaRodada> getPartidas() {
        if (partidas == null) {
            partidas = new ArrayList<>();
        }
        return partidas;
    }

    public void setPartidas(List<PartidaRodada> partidas) {
        this.partidas = partidas;
    }

    public List<String> getConfrontosGerados() {
        if (confrontosGerados == null) {
            confrontosGerados = new ArrayList<>();
        }
        return confrontosGerados;
    }

    public void setConfrontosGerados(List<String> confrontosGerados) {
        this.confrontosGerados = confrontosGerados;
    }
}
