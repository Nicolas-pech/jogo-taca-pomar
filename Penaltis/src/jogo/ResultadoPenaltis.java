package jogo;

public class ResultadoPenaltis {

    private final int golsTimeA;
    private final int golsTimeB;

    public ResultadoPenaltis(int golsTimeA, int golsTimeB) {
        this.golsTimeA = golsTimeA;
        this.golsTimeB = golsTimeB;
    }

    public int getGolsTimeA() {
        return golsTimeA;
    }

    public int getGolsTimeB() {
        return golsTimeB;
    }
}
