package entidades;

import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Batedor {

    private double x;
    private double y;
    private final double xInicial;

    private final int largura = 120;
    private final int altura = 300;

    private final double velocidadeHorizontal = 2.8;
    private final double velocidadeAproximacao = 1.8;

    private int limiteEsquerdo;
    private int limiteDireito;

    private boolean movendoEsquerda;
    private boolean movendoDireita;

    private Image[] imagensTimeA = new Image[10];
    private Image[] imagensTimeB = new Image[10];
    private Image[] imagensCorrida = imagensTimeA;

    // Índice da imagem atualmente exibida
    private int quadroAtual = 0;

    // Controla a velocidade da animação
    private int contadorAnimacao = 0;
    private final int intervaloAnimacao = 2;
    private int framesAnimacaoChute = 0;
    private final double yInicial;
    private final double yAlvoAproximacao;
    private boolean aproximandoDaBola;

    public Batedor(
            double x,
            double y,
            int limiteEsquerdo,
            int limiteDireito
    ) {

        this.x = x;
        this.y = y;
        this.xInicial = x;
        this.yInicial = y;
        this.yAlvoAproximacao = y - 130;

        this.limiteEsquerdo = limiteEsquerdo;
        this.limiteDireito = limiteDireito;

        carregarImagens();
    }

    private void carregarImagens() {

        for (int i = 0; i < imagensTimeA.length; i++) {
            imagensTimeA[i] = new ImageIcon("src/imagens/jogador/jogador_time_a_" + (i + 1) + ".png").getImage();
            imagensTimeB[i] = new ImageIcon("src/imagens/jogador/jogador_time_b_" + (i + 1) + ".png").getImage();
        }
    }

    public void atualizar() {

        if (movendoEsquerda) {
            x -= velocidadeHorizontal;
        }

        if (movendoDireita) {
            x += velocidadeHorizontal;
        }

        limitarMovimento();

        boolean estaMovendo = movendoEsquerda || movendoDireita;
        boolean animarChute = framesAnimacaoChute > 0;

        if (estaMovendo || animarChute) {

            contadorAnimacao++;

            if (contadorAnimacao >= intervaloAnimacao) {

                quadroAtual++;

                if (quadroAtual >= imagensCorrida.length) {
                    quadroAtual = 0;
                }

                if (aproximandoDaBola) {
                    y -= velocidadeAproximacao;
                    if (y <= yAlvoAproximacao) {
                        y = yAlvoAproximacao;
                        aproximandoDaBola = false;
                    }
                }

                contadorAnimacao = 0;
            }

            if (framesAnimacaoChute > 0) {
                framesAnimacaoChute--;
                if (framesAnimacaoChute == 0) {
                    aproximandoDaBola = false;
                }
            }

        } else {
            quadroAtual = 0;
            contadorAnimacao = 0;
        }
    }

    private void limitarMovimento() {

        if (x < limiteEsquerdo) {
            x = limiteEsquerdo;
        }

        if (x + largura > limiteDireito) {
            x = limiteDireito - largura;
        }
    }

    public void desenhar(Graphics2D g2) {

        g2.drawImage(
                imagensCorrida[quadroAtual],
                (int) x,
                (int) y,
                largura,
                altura,
                null
        );
    }

    public void moverEsquerda(boolean mover) {
        movendoEsquerda = mover;
    }

    public void moverDireita(boolean mover) {
        movendoDireita = mover;
    }

    public void definirSpritesTimeA(boolean usarTimeA) {
        imagensCorrida = usarTimeA ? imagensTimeA : imagensTimeB;
        quadroAtual = 0;
        contadorAnimacao = 0;
    }

    public void iniciarAnimacaoChute() {
        framesAnimacaoChute = imagensCorrida.length * 2;
        aproximandoDaBola = true;
    }

    public void finalizarAnimacaoChute() {
        aproximandoDaBola = false;
        y = yInicial;
    }

    public void reiniciarPosicao() {
        x = xInicial;
        y = yInicial;
        movendoEsquerda = false;
        movendoDireita = false;
        aproximandoDaBola = false;
        framesAnimacaoChute = 0;
        quadroAtual = 0;
        contadorAnimacao = 0;
    }

    public boolean isAproximandoDaBola() {
        return aproximandoDaBola;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public double getCentroX() {
        return x + largura / 2.0;
    }

    public double getBolaY() {
        return y - 25;
    }
}
