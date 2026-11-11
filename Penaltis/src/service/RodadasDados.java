package service;

import java.util.ArrayList;
import java.util.List;

public class RodadasDados {

    private List<TemporadaRodadas> temporadas;

    public RodadasDados() {
        this.temporadas = new ArrayList<>();
    }

    public List<TemporadaRodadas> getTemporadas() {
        if (temporadas == null) {
            temporadas = new ArrayList<>();
        }
        return temporadas;
    }

    public void setTemporadas(List<TemporadaRodadas> temporadas) {
        this.temporadas = temporadas;
    }
}
