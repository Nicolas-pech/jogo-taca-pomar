package jogo;

import entidades.Batedor;
import entidades.Bola;
import entidades.Campo;
import entidades.Goleiro;
import java.awt.Graphics2D;
import java.util.Objects;
import java.util.Random;

public class Jogo {

    private static final int TOTAL_COBRANCAS_POR_TIME = 5;

    private Campo campo;
    private Batedor batedor;
    private Goleiro goleiro;
    private Bola bola;
    private final double bolaInicialX;
    private final double bolaInicialY;
    private final double bolaInicialYAlta;
    private final Random random;
    private final String nomeTimeA;
    private final String nomeTimeB;
    private DirecaoChute direcaoChute;
    private boolean cobrancaEmAndamento;
    private boolean ultimaCobrancaFoiGol;
    private boolean ultimaCobrancaTimeA;
    private boolean disputaFinalizada;
    private boolean vezTimeA;
    private boolean aguardandoFinalizarMovimentoBatedor;
    private boolean aguardandoProximaCobranca;
    private boolean chuteSuperiorPendente;
    private double velocidadeXBolaPendente;
    private double velocidadeYBolaPendente;
    private double velocidadeBaseBolaPendente;
    private double alvoXBolaPendente;
    private double alvoYBolaPendente;
    private int cobrancasTimeA;
    private int cobrancasTimeB;
    private int golsTimeA;
    private int golsTimeB;
    private String mensagem;
    private String resultadoUltimaBatida;
    private boolean aguardandoMostrarResultadoFinal;
    private boolean exibindoResultadoFinal;
    private boolean resultadoFinalConfirmado;

    public Jogo(int larguraTela, int alturaTela) {
        this(larguraTela, alturaTela, "Time A", "Time B");
    }

    public Jogo(int larguraTela, int alturaTela, String nomeTimeA, String nomeTimeB) {

        campo = new Campo(
                larguraTela,
                alturaTela
        );

        batedor = new Batedor(
                campo.getPenaltiX() - 110,
                campo.getPenaltiY() - 150,
                campo.getX(),
                campo.getX() + campo.getLargura()
        );

        bola = new Bola(
                campo.getPenaltiX() - 10,
                campo.getPenaltiY() - 10
        );

        bolaInicialX = bola.getX();
        bolaInicialY = bola.getY();
        bolaInicialYAlta = bolaInicialY - 18;
        direcaoChute = DirecaoChute.ESQUERDA_INFERIOR;

        goleiro = new Goleiro(
                campo.getGolX() + campo.getGolLargura() / 2.0 - 376,
                campo.getGolY() + 150,
                campo.getGolX(),
                campo.getGolX() + campo.getGolLargura(),
                campo.getGolY()
        );

        this.random = new Random();
        this.nomeTimeA = Objects.requireNonNull(nomeTimeA, "nomeTimeA");
        this.nomeTimeB = Objects.requireNonNull(nomeTimeB, "nomeTimeB");
        this.vezTimeA = true;
        batedor.definirSpritesTimeA(true);
        this.mensagem = "Escolha o canto e pressione ESPACO para bater.";
        this.resultadoUltimaBatida = "";
    }

    public void atualizar() {
        batedor.atualizar();
        goleiro.atualizar();
        liberarChuteQuandoPronto();
        bola.atualizar();
        verificarFimDoChute();
    }

    public void desenhar(Graphics2D g2) {
        campo.desenhar(g2);
        goleiro.desenhar(g2);
        bola.desenhar(g2);
        batedor.desenhar(g2);
    }

    public Batedor getBatedor() {
        return batedor;
    }

    public Goleiro getGoleiro() {
        return goleiro;
    }

    public Bola getBola() {
        return bola;
    }

    public Campo getCampo() {
        return campo;
    }

    public DirecaoChute getDirecaoChute() {
        return direcaoChute;
    }

    public String getTextoDirecao() {
        switch (direcaoChute) {
            case ESQUERDA_SUPERIOR:
                return "ESQ SUP";
            case DIREITA_SUPERIOR:
                return "DIR SUP";
            case DIREITA_INFERIOR:
                return "DIR INF";
            default:
                return "ESQ INF";
        }
    }

    public String getTextoDefesa() {
        switch (goleiro.getDirecaoDefesa()) {
            case ESQUERDA_SUPERIOR:
                return "ESQ SUP";
            case DIREITA_SUPERIOR:
                return "DIR SUP";
            case DIREITA_INFERIOR:
                return "DIR INF";
            default:
                return "ESQ INF";
        }
    }

    public void chutar() {
        if (bola.isEmMovimento() || cobrancaEmAndamento || aguardandoFinalizarMovimentoBatedor) {
            return;
        }

        if (disputaFinalizada) {
            if (aguardandoMostrarResultadoFinal) {
                aguardandoMostrarResultadoFinal = false;
                exibindoResultadoFinal = true;
                resultadoFinalConfirmado = false;
                resultadoUltimaBatida = "";
                mensagem = "Resultado final exibido.";
            }
            return;
        }

        if (aguardandoProximaCobranca) {
            aguardandoProximaCobranca = false;
            goleiro.prepararProximaCobranca();
            bola.parar();
            bola.setX(bolaInicialX);
            bola.setY(bolaInicialY);
            vezTimeA = !vezTimeA;
            batedor.definirSpritesTimeA(vezTimeA);
            resultadoUltimaBatida = "";
            mensagem = "Escolha o canto e pressione ESPACO para bater.";
            return;
        }

        goleiro.escolherDefesa();
        goleiro.parar();
        goleiro.moverParaDefesa();
        ultimaCobrancaFoiGol = !direcaoChute.name().equals(goleiro.getDirecaoDefesa().name());
        ultimaCobrancaTimeA = vezTimeA;
        cobrancaEmAndamento = true; 
        aguardandoFinalizarMovimentoBatedor = true;
        bola.parar();
        bola.setX(bolaInicialX);
        bola.setY(bolaInicialY);
        batedor.iniciarAnimacaoChute();
        chuteSuperiorPendente = direcaoChute == DirecaoChute.ESQUERDA_SUPERIOR
                || direcaoChute == DirecaoChute.DIREITA_SUPERIOR;
        velocidadeBaseBolaPendente = chuteSuperiorPendente ? 12.4 : 10.8;
        double[] alvo = calcularAlvoNoGol(direcaoChute);
        alvoXBolaPendente = alvo[0];
        alvoYBolaPendente = alvo[1];
    }

    public void iniciarNovaPartida() {
        if (!exibindoResultadoFinal) {
            return;
        }

        bola.parar();
        bola.setX(bolaInicialX);
        bola.setY(bolaInicialY);
        batedor.reiniciarPosicao();
        goleiro.reiniciarPosicao();

        direcaoChute = DirecaoChute.ESQUERDA_INFERIOR;
        cobrancaEmAndamento = false;
        ultimaCobrancaFoiGol = false;
        ultimaCobrancaTimeA = false;
        disputaFinalizada = false;
        vezTimeA = true;
        aguardandoFinalizarMovimentoBatedor = false;
        aguardandoProximaCobranca = false;
        chuteSuperiorPendente = false;
        velocidadeXBolaPendente = 0;
        velocidadeYBolaPendente = 0;
        velocidadeBaseBolaPendente = 0;
        alvoXBolaPendente = 0;
        alvoYBolaPendente = 0;
        cobrancasTimeA = 0;
        cobrancasTimeB = 0;
        golsTimeA = 0;
        golsTimeB = 0;
        resultadoUltimaBatida = "";
        aguardandoMostrarResultadoFinal = false;
        exibindoResultadoFinal = false;
        resultadoFinalConfirmado = false;
        batedor.definirSpritesTimeA(true);
        mensagem = "Escolha o canto e pressione ESPACO para bater.";
    }

    public void confirmarResultadoFinal() {
        if (exibindoResultadoFinal) {
            resultadoFinalConfirmado = true;
        }
    }

    private void liberarChuteQuandoPronto() {
        if (!aguardandoFinalizarMovimentoBatedor) {
            return;
        }
        if (batedor.isAproximandoDaBola()) {
            return;
        }

        if (chuteSuperiorPendente) {
            bola.setY(bolaInicialYAlta);
        } else {
            bola.setY(bolaInicialY);
        }
        double[] velocidade = calcularVelocidadeParaAlvo(
                bola.getX(),
                bola.getY(),
                alvoXBolaPendente,
                alvoYBolaPendente,
                velocidadeBaseBolaPendente
        );
        velocidadeXBolaPendente = velocidade[0];
        velocidadeYBolaPendente = velocidade[1];
        bola.chutar(velocidadeXBolaPendente, velocidadeYBolaPendente);
        aguardandoFinalizarMovimentoBatedor = false;
    }

    private void verificarFimDoChute() {
        if (!bola.isEmMovimento() || !cobrancaEmAndamento) {
            return;
        }

        if (devePararBolaNoAlvo()) {
            bola.parar();
            bola.setX(alvoXBolaPendente);
            bola.setY(alvoYBolaPendente);
            batedor.finalizarAnimacaoChute();
            concluirCobranca();
        }
    }

    private boolean devePararBolaNoAlvo() {
        double distanciaRestante = Math.hypot(
                alvoXBolaPendente - bola.getX(),
                alvoYBolaPendente - bola.getY()
        );
        double distanciaPorFrame = Math.hypot(
                bola.getVelocidadeX(),
                bola.getVelocidadeY()
        );
        return distanciaRestante <= distanciaPorFrame;
    }

    private double[] calcularVelocidadeParaAlvo(
            double origemX,
            double origemY,
            double alvoX,
            double alvoY,
            double velocidadeBase
    ) {
        double deltaX = alvoX - origemX;
        double deltaY = alvoY - origemY;
        double distancia = Math.hypot(deltaX, deltaY);
        if (distancia == 0) {
            return new double[]{0, 0};
        }
        return new double[]{
            (deltaX / distancia) * velocidadeBase,
            (deltaY / distancia) * velocidadeBase
        };
    }

    private double[] calcularAlvoNoGol(DirecaoChute direcao) {
        double margemLateral = -220;
        double margemSuperior = 160;
        double faixaInferiorY = campo.getGolY() + 415;
        double xEsquerda = campo.getGolX() + margemLateral;
        double xDireita = campo.getGolX() + campo.getGolLargura() - bola.getTamanho() - margemLateral;
        double ySuperior = campo.getGolY() + margemSuperior;

        if (direcao == DirecaoChute.ESQUERDA_SUPERIOR) {
            return new double[]{xEsquerda, ySuperior};
        } else if (direcao == DirecaoChute.DIREITA_SUPERIOR) {
            return new double[]{xDireita, ySuperior};
        } else if (direcao == DirecaoChute.DIREITA_INFERIOR) {
            return new double[]{xDireita, faixaInferiorY};
        }
        return new double[]{xEsquerda, faixaInferiorY};
    }

    private void concluirCobranca() {
        resultadoUltimaBatida = ultimaCobrancaFoiGol ? "GOL" : "DEFESA";
        if (ultimaCobrancaFoiGol) {
            if (ultimaCobrancaTimeA) {
                golsTimeA++;
                mensagem = "GOL do " + nomeTimeA + "!";
            } else {
                golsTimeB++;
                mensagem = "GOL do " + nomeTimeB + "!";
            }
        } else if (ultimaCobrancaTimeA) {
            mensagem = "DEFESA do goleiro! " + nomeTimeA + " perdeu.";
        } else {
            mensagem = "DEFESA do goleiro! " + nomeTimeB + " perdeu.";
        }

        if (ultimaCobrancaTimeA) {
            cobrancasTimeA++;
        } else {
            cobrancasTimeB++;
        }

        cobrancaEmAndamento = false;
        if (deveFinalizarDisputa()) {
            disputaFinalizada = true;
            aguardandoMostrarResultadoFinal = true;
            mensagem = "Pressione ESPACO para ver o resultado final.";
            return;
        }
        aguardandoProximaCobranca = true;
        mensagem += " Pressione ESPACO para a proxima batida.";
    }

    private boolean deveFinalizarDisputa() {
        if (cobrancasTimeA >= TOTAL_COBRANCAS_POR_TIME && cobrancasTimeB >= TOTAL_COBRANCAS_POR_TIME) {
            return true;
        }
        int restantesA = TOTAL_COBRANCAS_POR_TIME - cobrancasTimeA;
        int restantesB = TOTAL_COBRANCAS_POR_TIME - cobrancasTimeB;
        if (golsTimeA > golsTimeB + restantesB) {
            return true;
        }
        return golsTimeB > golsTimeA + restantesA;
    }

    public void escolherEsquerdaSuperior() {
        if (!bola.isEmMovimento() && !disputaFinalizada && !cobrancaEmAndamento) {
            direcaoChute = DirecaoChute.ESQUERDA_SUPERIOR;
        }
    }

    public void escolherDireitaSuperior() {
        if (!bola.isEmMovimento() && !disputaFinalizada && !cobrancaEmAndamento) {
            direcaoChute = DirecaoChute.DIREITA_SUPERIOR;
        }
    }

    public void escolherEsquerdaInferior() {
        if (!bola.isEmMovimento() && !disputaFinalizada && !cobrancaEmAndamento) {
            direcaoChute = DirecaoChute.ESQUERDA_INFERIOR;
        }
    }

    public void escolherDireitaInferior() {
        if (!bola.isEmMovimento() && !disputaFinalizada && !cobrancaEmAndamento) {
            direcaoChute = DirecaoChute.DIREITA_INFERIOR;
        }
    }

    public String getNomeTimeA() {
        return nomeTimeA;
    }

    public String getNomeTimeB() {
        return nomeTimeB;
    }

    public int getGolsTimeA() {
        return golsTimeA;
    }

    public int getGolsTimeB() {
        return golsTimeB;
    }

    public int getCobrancasTimeA() {
        return cobrancasTimeA;
    }

    public int getCobrancasTimeB() {
        return cobrancasTimeB;
    }

    public int getTotalCobrancasPorTime() {
        return TOTAL_COBRANCAS_POR_TIME;
    }

    public boolean isVezTimeA() {
        return vezTimeA;
    }

    public boolean isDisputaFinalizada() {
        return disputaFinalizada;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getResultadoUltimaBatida() {
        return resultadoUltimaBatida;
    }

    public boolean isExibindoResultadoFinal() {
        return exibindoResultadoFinal;
    }

    public boolean isResultadoFinalConfirmado() {
        return resultadoFinalConfirmado;
    }

    public String getTextoResultadoFinal() {
        if (golsTimeA > golsTimeB) {
            return nomeTimeA + " venceu!";
        }
        if (golsTimeB > golsTimeA) {
            return nomeTimeB + " venceu!";
        }
        return "Empate!";
    }

    public String getPlacarFinal() {
        return golsTimeA + " x " + golsTimeB;
    }

    public DirecaoDefesa getDirecaoDefesaAtual() {
        return goleiro.getDirecaoDefesa();
    }

    public void definirSementeDefesa(long semente) {
        random.setSeed(semente);
    }

    public int sortearIndiceDefesa() {
        return random.nextInt(4);
    }
}
