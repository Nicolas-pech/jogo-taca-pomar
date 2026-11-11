package entidades;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import jogo.DirecaoDefesa;

public class Goleiro {

    private double x;
    private double y;
    private final double xBase;

    private final int largura = 750;
    private final int altura = 320;

    private final double yBase;

    private boolean emMovimento = true;
    private boolean emAnimacao;
    private int quadroAnimacao;
    private int passoAnimacao;
    private final int intervaloAnimacao = 8;
    private int totalPassosAnimacao;
    private final Random random = new Random();
    private Image imagemParada;
    private Image[] animacaoAtual;
    private Image[] animacaoEsquerdaSuperior;
    private Image[] animacaoDireitaSuperior;
    private Image[] animacaoEsquerdaInferior;
    private Image[] animacaoDireitaInferior;

    private DirecaoDefesa direcaoDefesa;

    public Goleiro(double x, double y, int limiteEsquerdo, int limiteDireito, int topoGol) {
        this.x = x;
        this.y = y;
        this.xBase = x;
        this.yBase = y;
        direcaoDefesa = DirecaoDefesa.ESQUERDA_INFERIOR;
        carregarImagens();
    }

    private void carregarImagens() {
        imagemParada = new ImageIcon("src/imagens/goleiro/goleiro.png").getImage();
        animacaoEsquerdaSuperior = carregarSequencia("src/imagens/goleiro/goleiro_direito_superior_");
        animacaoDireitaSuperior = carregarSequencia("src/imagens/goleiro/goleiro_esquerdo_superior_");
        animacaoEsquerdaInferior = carregarSequencia("src/imagens/goleiro/goleiro_direito_inferior_");
        animacaoDireitaInferior = carregarSequencia("src/imagens/goleiro/goleiro_esquerdo_inferior_");
        animacaoAtual = animacaoEsquerdaInferior;
    }

    private Image[] carregarSequencia(String prefixo) {
        Image[] imagens = new Image[4];
        for (int i = 0; i < imagens.length; i++) {
            imagens[i] = new ImageIcon(prefixo + (i + 1) + ".png").getImage();
        }
        return imagens;
    }

    public void atualizar() {
        if (emAnimacao) {
            passoAnimacao++;

            quadroAnimacao = passoAnimacao / intervaloAnimacao;
            if (quadroAnimacao >= animacaoAtual.length) {
                quadroAtualFinal();
            }

            if (passoAnimacao >= totalPassosAnimacao) {
                emAnimacao = false;
                quadroAtualFinal();
            }
            return;
        }

        if (!emMovimento) {
            return;
        }
        y = yBase;
    }

    public void parar() {
        emMovimento = false;
    }

    public void escolherDefesa() {
        int escolha = random.nextInt(4);
        switch (escolha) {
            case 0:
                direcaoDefesa = DirecaoDefesa.ESQUERDA_SUPERIOR;
                break;
            case 1:
                direcaoDefesa = DirecaoDefesa.DIREITA_SUPERIOR;
                break;
            case 2:
                direcaoDefesa = DirecaoDefesa.ESQUERDA_INFERIOR;
                break;
            default:
                direcaoDefesa = DirecaoDefesa.DIREITA_INFERIOR;
                break;
        }
    }

    public void moverParaDefesa() {
        emAnimacao = true;
        quadroAnimacao = 0;
        passoAnimacao = 0;
        switch (direcaoDefesa) {
            case ESQUERDA_SUPERIOR:
                animacaoAtual = animacaoEsquerdaSuperior;
                break;
            case DIREITA_SUPERIOR:
                animacaoAtual = animacaoDireitaSuperior;
                break;
            case ESQUERDA_INFERIOR:
                animacaoAtual = animacaoEsquerdaInferior;
                break;
            default:
                animacaoAtual = animacaoDireitaInferior;
                break;
        }
        totalPassosAnimacao = animacaoAtual.length * intervaloAnimacao * 2;
    }

    public void prepararProximaCobranca() {
        emMovimento = true;
        emAnimacao = false;
        quadroAnimacao = 0;
        passoAnimacao = 0;
        y = yBase;
    }

    public void reiniciarPosicao() {
        x = xBase;
        y = yBase;
        emMovimento = true;
        emAnimacao = false;
        quadroAnimacao = 0;
        passoAnimacao = 0;
        animacaoAtual = animacaoEsquerdaInferior;
        direcaoDefesa = DirecaoDefesa.ESQUERDA_INFERIOR;
    }

    private void quadroAtualFinal() {
        quadroAnimacao = animacaoAtual.length - 1;
    }

    public void desenhar(Graphics2D g2) {
        Image imagem = imagemParada;
        if ((emAnimacao || !emMovimento) && animacaoAtual != null && animacaoAtual.length > 0) {
            imagem = animacaoAtual[Math.min(quadroAnimacao, animacaoAtual.length - 1)];
        }
        g2.drawImage(imagem, (int) x, (int) y, largura, altura, null);
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

    public DirecaoDefesa getDirecaoDefesa() {
        return direcaoDefesa;
    }
}
